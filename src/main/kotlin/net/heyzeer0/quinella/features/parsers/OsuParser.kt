package net.heyzeer0.quinella.features.parsers

import com.github.francesco149.koohii.Koohii
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.heyzeer0.quinella.core.configs.apiKeys
import net.heyzeer0.quinella.core.currentTimeMillis
import net.heyzeer0.quinella.core.toBoolean
import net.heyzeer0.quinella.databaseManager
import net.heyzeer0.quinella.features.instances.OsuBeatMapProfile
import net.heyzeer0.quinella.features.instances.OsuMatchProfile
import net.heyzeer0.quinella.features.instances.OsuOppaiAnalyse
import net.heyzeer0.quinella.features.instances.OsuUserProfile
import net.heyzeer0.quinella.httpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object OsuParser {

    private const val USER_PROFILE = "https://osu.ppy.sh/api/get_user?k=%s&u=%s"
    private const val BEATMAP_PROFILE = "https://osu.ppy.sh/api/get_beatmaps?k=%S&b=%s"
    private const val USER_BEST = "https://osu.ppy.sh/api/get_user_best?k=%S&limit=%s&u=%s"
    private const val USER_RECENT = "https://osu.ppy.sh/api/get_user_recent?k=%S&limit=%s&u=%s"
    private const val BEATMAP_DOWNLOAD = "https://osu.ppy.sh/osu/%s"

    private val jsonParser = JsonParser()
    private val beatMapCache = WeakHashMap<String, Koohii.Map>()

    /**
     * Requests a user profile based on his username or userId
     *
     * @param user the user name or user id
     * @param isId Optional | is the provided user name an id?
     * @return an OsuUserProfile object containing all the available user info
     */
    fun requestUserProfile(user: String, isId: Boolean = false): OsuUserProfile? {
            var finalUrl = USER_PROFILE.format(apiKeys!!.osuKey, user)
            if (isId) finalUrl += "&type=id"

            val request = Request.Builder().url(finalUrl).build()
            val response = httpClient.newCall(request).execute()
            val jsonResponse = jsonParser.parse(response.body()?.string()).asJsonArray

            if (jsonResponse.size() == 0) return null // player not found

            val userObject = jsonResponse[0].asJsonObject

            return OsuUserProfile(
                userObject["user_id"].asString,
                userObject["username"].asString,
                userObject["join_date"].asString,
                userObject["country"].asString,
                userObject["count300"].asString.toLong(),
                userObject["count100"].asString.toLong(),
                userObject["count50"].asString.toLong(),
                userObject["playcount"].asString.toInt(),
                userObject["ranked_score"].asString.toLong(),
                userObject["total_score"].asString.toLong(),
                userObject["pp_rank"].asString.toInt(),
                userObject["pp_country_rank"].asString.toInt(),
                userObject["level"].asString.toDouble(),
                userObject["pp_raw"].asString.toDouble(),
                userObject["accuracy"].asString.toDouble(),
                userObject["count_rank_ss"].asString.toInt(),
                userObject["count_rank_ssh"].asString.toInt(),
                userObject["count_rank_s"].asString.toInt(),
                userObject["count_rank_sh"].asString.toInt(),
                userObject["count_rank_a"].asString.toInt()
            )
    }

    /**
     * Requests a BeatMap profile
     *
     * @param id the BeatMap id
     * @return an instance containing all the BeatMap information
     */
    fun requestBeatMapProfile(id: String): OsuBeatMapProfile? {
        val request = Request.Builder().url(BEATMAP_PROFILE.format(apiKeys!!.osuKey, id)).build()
        val response = httpClient.newCall(request).execute()
        val jsonResponse = jsonParser.parse(response.body()?.string()).asJsonArray

        if (jsonResponse.size() == 0) return null // beatmap not found

        val beatMapObject = jsonResponse[0].asJsonObject

        return OsuBeatMapProfile(
                beatMapObject["beatmap_id"].asString,
                beatMapObject["beatmapset_id"].asString,
                beatMapObject["approved"].asString.toInt().toBoolean(),
                beatMapObject["artist"].asString,
                beatMapObject["title"].asString,
                beatMapObject["total_length"].asString.toInt(),
                beatMapObject["hit_length"].asString.toInt(),
                beatMapObject["version"].asString,
                beatMapObject["diff_size"].asString.toDouble(),
                beatMapObject["diff_overall"].asString.toDouble(),
                beatMapObject["diff_approach"].asString.toDouble(),
                beatMapObject["diff_drain"].asString.toDouble(),
                beatMapObject["diff_aim"].asString.toDouble(),
                beatMapObject["diff_speed"].asString.toDouble(),
                beatMapObject["difficultyrating"].asString.toDouble(),
                beatMapObject["bpm"].asString.toInt(),
                beatMapObject["max_combo"].asString.toInt(),
                beatMapObject["mode"].asString.toInt(),
                beatMapObject["count_normal"].asString.toInt(),
                beatMapObject["count_slider"].asString.toInt(),
                beatMapObject["count_spinner"].asString.toInt(),
                beatMapObject["submit_date"].asString,
                beatMapObject["approved_date"].asString,
                beatMapObject["last_update"].asString,
                beatMapObject["creator"].asString,
                beatMapObject["creator_id"].asString,
                beatMapObject["favourite_count"].asString.toInt(),
                beatMapObject["rating"].asString.toDouble(),
                beatMapObject["playcount"].asString.toInt(),
                beatMapObject["passcount"].asString.toInt()
        )
    }

    /**
     * Requests a list of user best plays
     *
     * @param user the user name or id
     * @param limit the amount of plays to get
     * @return a list containing all match instances
     */
    fun requestUserBests(user: String, limit: Int = 10): List<OsuMatchProfile> {
        val result = arrayListOf<OsuMatchProfile>()

        val request = Request.Builder().url(USER_BEST.format(apiKeys!!.osuKey, limit, user)).build()
        val response = httpClient.newCall(request).execute()
        val jsonResponse = jsonParser.parse(response.body()?.string()).asJsonArray

        for (obj in jsonResponse) {
            result += parseMatchProfile(obj.asJsonObject)
        }

        return result;
    }

    /**
     * Requests a list of user recent plays
     *
     * @param user the user name or id
     * @param limit the amount of plays to get
     * @return a list containing all match instances
     */
    fun requestUserRecent(user: String, limit: Int = 10): List<OsuMatchProfile> {
        val result = arrayListOf<OsuMatchProfile>()

        val request = Request.Builder().url(USER_RECENT.format(apiKeys!!.osuKey, limit, user)).build()
        val response = httpClient.newCall(request).execute()
        val jsonResponse = jsonParser.parse(response.body()?.string()).asJsonArray

        for (obj in jsonResponse) {
            result += parseMatchProfile(obj.asJsonObject)
        }

        return result;
    }

    /**
     * Parses a JsonObject into the OsuMatchProfile instance
     *
     * @param matchObject the JsonObject containing all needed info
     * @return the translated pojo instance
     */
    private fun parseMatchProfile(matchObject: JsonObject): OsuMatchProfile {
        return OsuMatchProfile(
            matchObject["beatmap_id"].asString,
            matchObject["score_id"]?.asString,
            matchObject["user_id"].asString,
            matchObject["score"].asString.toInt(),
            matchObject["maxcombo"].asString.toInt(),
            matchObject["count50"].asString.toInt(),
            matchObject["count100"].asString.toInt(),
            matchObject["count300"].asString.toInt(),
            matchObject["countmiss"].asString.toInt(),
            matchObject["perfect"].asString.toInt().toBoolean(),
            matchObject["enabled_mods"].asString.toInt(),
            matchObject["rank"].asString,
            matchObject["pp"]?.asString?.toDouble(),
            matchObject["replay_available"]?.asString?.toInt()?.toBoolean()
        )
    }

    /**
     * Requests a performance analyse of the specified mapId
     * Takes some time to process and should be executed async
     *
     * @param mapId The BeatMap ID
     * @param mods Optional | The sum of the used mods
     * @param combo Optional | The combo amount
     * @param hit300 Optional | The 300x hit amount
     * @param hit100 Optional | The 100x hit amount
     * @param hit50 Optional | The 50x hit amount
     * @param misses Optional | The miss amount
     */
    fun requestBeatMapAnalyse(mapId: String, mods: Int = 0, combo: Int = -1, hit300: Int = -1, hit100: Int = 0, hit50: Int = 0, misses: Int = 0): OsuOppaiAnalyse? {
        if (combo == -1) { // if it's a user play (not a scan play) ignore the database entry
            val map =
                databaseManager.getServerProfile().osuAnalysedBeatMaps.firstOrNull { c -> c.mapId == mapId && c.mods == mods }
            if (map != null) return map // TODO ignore database if entry is older than 3 months
        }

        val beatMap: Koohii.Map?
        beatMap = if (beatMapCache.containsKey(mapId)) beatMapCache[mapId]
        else {
            val request = Request.Builder().url(BEATMAP_DOWNLOAD.format(mapId)).build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful || response.body() == null) return null

            Koohii.Parser().map(BufferedReader(InputStreamReader(response.body()!!.byteStream())))
        }
        beatMapCache[mapId] = beatMap // makes the weak reference stronger

        if (beatMap == null) return null

        val stars = Koohii.DiffCalc().calc(beatMap, mods)
        val parameters = Koohii.PPv2Parameters()
        parameters.aim_stars = stars.aim
        parameters.speed_stars = stars.speed;
        parameters.combo = combo
        parameters.n300 = hit300
        parameters.n100 = hit100
        parameters.n50 = hit50
        parameters.nmiss = misses
        parameters.beatmap = beatMap
        parameters.mods = mods

        val pp = Koohii.PPv2(parameters)
        return OsuOppaiAnalyse(mapId, currentTimeMillis(), pp.acc, pp.total, pp.speed, pp.aim, beatMap.ncircles, beatMap.nsliders, mods)
    }

}