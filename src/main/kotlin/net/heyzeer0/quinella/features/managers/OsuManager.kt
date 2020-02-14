@file:Suppress("SENSELESS_COMPARISON")

package net.heyzeer0.quinella.features.managers

import net.heyzeer0.quinella.core.enums.AvailableFont
import net.heyzeer0.quinella.core.enums.ImageTemplate
import net.heyzeer0.quinella.core.runAsync
import net.heyzeer0.quinella.core.utilities.*
import net.heyzeer0.quinella.databaseManager
import net.heyzeer0.quinella.features.containers.OppaiQueueContainer
import net.heyzeer0.quinella.features.enums.OsuModifier
import net.heyzeer0.quinella.features.instances.OsuOppaiAnalyse
import net.heyzeer0.quinella.features.parsers.OsuParser
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.roundToInt

object OsuManager {

    private val analyseQueue = LinkedBlockingQueue<OppaiQueueContainer>()
    private var processing = false;
    private val profileCache = WeakHashMap<String, BufferedImage>()

    fun queueAnalyse(mapId: String, onFinish: ((OsuOppaiAnalyse?) -> Unit)? = null, mods: Int = 0, combo: Int = -1, hit300: Int = -1, hit100: Int = 0, hit50: Int = 0, misses: Int = 0) {
        analyseQueue.add(OppaiQueueContainer(mapId, onFinish, mods, combo, hit300, hit100, hit50, misses))

        processQueue()
    }

    fun queueFullMapScan(mapId: String) {
        fun save(container: OsuOppaiAnalyse) {
            val serverProfile = databaseManager.getServerProfile()
            if (serverProfile.osuAnalysedBeatMaps.any { it.mapId == container.mapId && it.mods == container.mods }) return

            serverProfile.osuAnalysedBeatMaps.add(container)
            serverProfile.asyncSave()
        }

        // nomod
        queueAnalyse(mapId, {
            if (it == null) return@queueAnalyse
            save(it)
        })

        // hidden
        queueAnalyse(mapId, {
            if (it == null) return@queueAnalyse
            save(it)
        }, OsuModifier.HIDDEN.id)

        // hard rock
        queueAnalyse(mapId, {
            if (it == null) return@queueAnalyse
            save(it)
        }, OsuModifier.HARD_ROCK.id)

        // double time
        queueAnalyse(mapId, {
            if (it == null) return@queueAnalyse
            save(it)
        }, OsuModifier.DOUBLE_TIME.id)

        // hidden + double time
        queueAnalyse(mapId, {
            if (it == null) return@queueAnalyse
            save(it)
        }, OsuModifier.HIDDEN.id or OsuModifier.DOUBLE_TIME.id)

        // hard rock + double time
        queueAnalyse(mapId, {
            if (it == null) return@queueAnalyse
            save(it)
        }, OsuModifier.HARD_ROCK.id or OsuModifier.DOUBLE_TIME.id)
    }

    private fun processQueue(force: Boolean = false) {
        if (analyseQueue.isEmpty() || (processing && !force)) return
        processing = true

        runAsync {
            val container = analyseQueue.poll()

            val result = OsuParser.requestBeatMapAnalyse(container.mapId, container.mods, container.combo,
                container.hit300, container.hit100, container.hit50, container.misses)

            if (container.onFinish != null)
                container.onFinish!!.invoke(result)

            if (analyseQueue.isEmpty()) {
                processing = false
                return@runAsync
            }

            processQueue(true)
        }
    }

    fun drawUserProfileImage(user: String): BufferedImage? {
        if (profileCache.containsKey(user)) return profileCache[user] // check if cached
        val profile = OsuParser.requestUserProfile(user) ?: return null

        var matchText = "There's not a top play :("
        val bestMatches = OsuParser.requestUserBests(user, 50)
        bestMatches.forEach { queueFullMapScan(it.beatMapId)} // queue a complete scan to have more maps to suggest

        val bestMatch = bestMatches[0]
        if (bestMatch != null) {
            val beatMap = OsuParser.requestBeatMapProfile(bestMatch.beatMapId)
            if (beatMap != null) {
                matchText = "${beatMap.title} [${beatMap.version}] - ${bestMatch.performancePoints!!.roundToInt()}pp"
            }
        }

        val performanceText = "#${profile.performanceRank} | #${profile.localRank} - ${profile.performancePoints.roundToInt()}pp"

        val background = ImageTemplate.OSU_PLAYER_PROFILE.image.copy()
        var userAvatar = getImageFromUrl("https://a.ppy.sh/" + profile.id)
        val userFlag = getImageFromUrl("https://osu.ppy.sh/images/flags/${profile.country.toUpperCase()}.png")

        if (userAvatar == null) getImageFromUrl("https://osu.ppy.sh/images/layout/avatar-guest.png")
        if (userFlag == null) getImageFromUrl("https://osu.ppy.sh/images/flags/XX.png")

        if (userAvatar!!.width != 128 || userAvatar.height != 128)
            userAvatar = userAvatar.resize(128, 128)

        val graphics = background.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        graphics.drawImage(userAvatar, 52, 27, null) // user avatar
        graphics.drawImage(userFlag!!.resize(45, 30), 269, 109, null) // user flag

        // Italic Texts
        graphics.font = AvailableFont.EXO2_ITALIC.font.deriveFont(45.79f)
        graphics.drawStringWithWidthLimit(profile.name, 198, 80, 261)

        // Bold Texts
        graphics.font = AvailableFont.EXO2_BOLD.font
        graphics.drawCenteredString("${profile.level.roundToInt()}", Rectangle(205, 106, 42, 24), 23.5f) //level
        graphics.drawCenteredString("${profile.rankSSPlus}", Rectangle(536, 90, 56, 15), 21.38f) // SS+
        graphics.drawCenteredString("${profile.rankSS}", Rectangle(622, 90, 56, 15), 21.38f) // SS
        graphics.drawCenteredString("${profile.rankSPlus}", Rectangle(491, 147, 56, 15), 21.38f) // S+
        graphics.drawCenteredString("${profile.rankS}", Rectangle(580, 147, 56, 15), 21.38f) // S
        graphics.drawCenteredString("${profile.rankA}", Rectangle(666, 147, 56, 15), 21.38f) // A

        // End Texts
        graphics.font = graphics.font.deriveFont(18.21f)
        graphics.drawStringWithWidthLimit(matchText, 145, 215, 548)
        graphics.drawString(performanceText, 116, 237)

        graphics.dispose()

        profileCache[user] = background // add to weak cache
        return background
    }

}