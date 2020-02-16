package net.heyzeer0.quinella.commands

import com.github.francesco149.koohii.Koohii
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.heyzeer0.quinella.core.commands.annotations.Argument
import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.enums.ArgumentType
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.enums.ActionResult
import net.heyzeer0.quinella.core.enums.Emoji
import net.heyzeer0.quinella.core.getUserProfile
import net.heyzeer0.quinella.core.random
import net.heyzeer0.quinella.core.toReadableTime
import net.heyzeer0.quinella.databaseManager
import net.heyzeer0.quinella.features.instances.OsuOppaiAnalyse
import net.heyzeer0.quinella.features.managers.BlackDesertManager
import net.heyzeer0.quinella.features.managers.OsuManager
import net.heyzeer0.quinella.features.parsers.BlackDesert
import net.heyzeer0.quinella.features.parsers.OsuParser
import net.heyzeer0.quinella.features.parsers.RandomCat
import net.heyzeer0.quinella.features.parsers.RandomDog
import java.awt.Color
import java.util.function.Consumer
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.streams.toList

class FunCommands {

    @Command(name = "cat", type = CommandType.FUN, description = "Get a random cat!")
    fun randomCat(e: MessageTranslator, args: ArgumentTranslator) {
        val embedBuilder = EmbedBuilder()
        embedBuilder.setColor(Color.GREEN)
        embedBuilder.setImage(RandomCat.generateRandomCat())

        e.sendMessage(embedBuilder)
    }

    @Command(name = "dog", type = CommandType.FUN, description = "Get a random dog!")
    fun randomDog(e: MessageTranslator, args: ArgumentTranslator) {
        val embedBuilder = EmbedBuilder()
        embedBuilder.setColor(Color.GREEN)
        embedBuilder.setImage(RandomDog.generateRandomDog())

        e.sendMessage(embedBuilder)
    }

    @Command(name = "blackdesert", aliases = ["bdo"], type = CommandType.FUN, description = "Veja informações sobre os bosses de hoje!")
    fun blackDesert(e: MessageTranslator, args: ArgumentTranslator) {
        val bossId = BlackDesert.getNextBossId()

        e.sendMessage(":game_die: Os boses de hoje são: ``${BlackDesert.getTodayBosses()}``")
        e.sendMessage("**Proximo boss:** ``${BlackDesert.getBossById(bossId)}`` em ``${BlackDesert.getBossAsDate(bossId).toReadableTime()}``")
    }

    @Command(name = "blackdesert/register", aliases = ["bdo"], type = CommandType.FUN, permissions = [Permission.MANAGE_CHANNEL], description = "Registre seu canal para receber notificações")
    fun blackDesertRegister(e: MessageTranslator, args: ArgumentTranslator) {
        val channel = e.getChannel()

        when (BlackDesertManager.updateChannelStatus(channel.id)) {
            ActionResult.REMOVED -> e.sendMessage("${Emoji.ANIM_CORRECT} Este canal não irá mais receber notificações do jogo!")
            ActionResult.ADDED -> e.sendMessage("${Emoji.ANIM_CORRECT} Este canal agora irá receber notificações do jogo!")
        }
    }

    @Command(name = "osu", type = CommandType.FUN, description = "Lists all available osu related commands!")
    fun osu(e: MessageTranslator, args: ArgumentTranslator) {

    }

    // OSU COMMANDS BELOW

    @Command(name = "osu/setuser", type = CommandType.FUN, description = "Set your osu username",
        args = [ Argument(name = "u", type = ArgumentType.STRING, description = "The username", optional = false) ])
    fun osuSetUser(e: MessageTranslator, args: ArgumentTranslator) {
        val user = args.getAsString("u")!!

        val profile = OsuParser.requestUserProfile(user)
        if (profile == null) {
            e.sendMessage(Emoji.THINKING + "The provided user doesn't have an osu! account.")
            return
        }

        val userProfile = e.getAuthor().getUserProfile()
        userProfile.osuId = user
        userProfile.asyncSave()

        e.sendMessage(Emoji.ANIM_CORRECT + "Sucessfully set your osu! username!")
    }

    @Command(name = "osu/profile", type = CommandType.FUN, description = "See a osu player profile!",
        args = [ Argument(name = "u", type = ArgumentType.STRING, description = "The username or id") ])
    fun osuProfile(e: MessageTranslator, args: ArgumentTranslator) {
        val user = args.getAsString("u") ?: e.getAuthor().getUserProfile().osuId

        if (user == null) {
            e.sendMessage(Emoji.THINKING + "You need to specify a user name, you can do that by using:\n" +
                    Emoji.EMPTY + Emoji.ONE + "``!osu setuser -u {name}`` to set **permanently**\n" +
                    Emoji.EMPTY + Emoji.TWO + "``!osu recommend -u {name}`` to **specify a user** instead of you.")
            return
        }

        e.sendMessage(Emoji.ANIM_LOADING + "Generating **$user** profile...", Consumer {
            val image = OsuManager.drawUserProfileImage(user)

            if (image == null) {
                it.editMessage(Emoji.THINKING + "The provided user doesn't have an osu! account.").queue()
                return@Consumer
            }

            it.delete().queue()
            e.sendImage(image, null, user)
        })
    }

    @Command(name = "osu/recommend", type = CommandType.FUN, description = "Get a beatmap recomendation!", aliases = [ "rec" ],
        args = [ Argument(name = "u", type = ArgumentType.STRING, description = "The user name or id"),
                 Argument(name = "pp", type = ArgumentType.NUMBER, description = "The PP range to search"),
                 Argument(name = "mods", type = ArgumentType.STRING, description = "The mods that you want to play"),
                 Argument(name = "jump", type = ArgumentType.NUMBER, description = "The maximum PP jump")])
    fun osuRecommend(e: MessageTranslator, args: ArgumentTranslator) {
        val user = args.getAsString("u") ?: e.getAuthor().getUserProfile().osuId

        if (user == null) {
            e.sendMessage(Emoji.THINKING + "You need to specify a user name, you can do that by using:\n" +
                    Emoji.EMPTY + Emoji.ONE + "``!osu setuser -u {name}`` to set **permanently**\n" +
                    Emoji.EMPTY + Emoji.TWO + "``!osu recommend -u {name}`` to **specify a user** instead of you.")
            return
        }

        e.sendMessage(Emoji.ANIM_LOADING + "Calculating **" + user + "** Play Style...", Consumer {
            val userProfile = OsuParser.requestUserProfile(user)

            if (userProfile == null) {
                it.editMessage(Emoji.QUINELLA_THINK + "The provided user doesn't have an osu! account.")
                return@Consumer
            }

            // play style calculation
            val userBests = OsuParser.requestUserBests(user, 10)
            val beatMaps = userBests.map { OsuParser.requestBeatMapProfile(it.beatMapId)!! }.toList()
            val mapIds = beatMaps.map { it.id }.toList()

            val playStyle = OsuManager.calculateUserPlayStyle(user, userBests, beatMaps)
            it.editMessage(Emoji.ANIM_LOADING + "Collecting possible BeatMap...").queue() // update status

            var ppRange = args.getAsInteger("pp") ?: playStyle.performancePoints
            ppRange += random.nextInt(args.getAsInteger("jump") ?: 35) // adds a little offset to improve the user

            val mods = if (args.has("mods")) Koohii.mods_from_str(args.getAsString("mods")) else playStyle.mods

            // finding the best map possible
            val comparator = compareBy<OsuOppaiAnalyse>
                { abs(it.performancePoints - playStyle.performancePoints) } // by performnace
                .thenBy { abs(it.hitCircles - playStyle.hitCircles) } // by hit circles

            val recommendedMaps = databaseManager.getServerProfile().osuAnalysedBeatMaps.stream()
                .filter { c -> !mapIds.contains(c.mapId) && c.mods == mods }.sorted(comparator).limit(15).toList()

            if (recommendedMaps.isEmpty()) {
                it.editMessage(Emoji.ANGER + "I was not able to find a BeatMap for you!")
                return@Consumer
            }

            val selectedMap = recommendedMaps[random.nextInt(recommendedMaps.size)]
            val mapProfile = OsuParser.requestBeatMapProfile(selectedMap.mapId)!!

            // generating texts
            val ppText = "100% = ${selectedMap.performancePoints.roundToInt()}pp - Your % = in development..."
            val statusText = "Stars: ${selectedMap.stars.roundToInt()} - ${mapProfile.maxCombo}x -" +
                    " AR: ${selectedMap.ar.roundToInt()}" +
                    " OD: ${selectedMap.od.roundToInt()}" +
                    " HP: ${selectedMap.hp.roundToInt()}" +
                    " CS: ${selectedMap.cs.roundToInt()}" +
                    " BPM: ${mapProfile.bpm.roundToInt()}"

            // generating image + sending
            val image = OsuManager.drawBeatmapProfile(mapProfile, userProfile, ppText, statusText, selectedMap.mods)
            val embed = EmbedBuilder()
            embed.setColor(Color.GREEN)
            embed.setDescription("**Downloads**: [Normal](https://osu.ppy.sh/d/${mapProfile.setId})" +
                    " - [No Vid](https://osu.ppy.sh/d/${mapProfile.setId}n)" +
                    " - [Bloodcat](https://bloodcat.com/osu/s/${mapProfile.setId})")

            it.delete().queue()
            e.sendImage(image, embed, "rec-${mapProfile.id}")
        })
    }

}