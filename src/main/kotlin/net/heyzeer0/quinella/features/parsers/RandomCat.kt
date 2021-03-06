package net.heyzeer0.quinella.features.parsers

import com.google.gson.JsonParser
import net.heyzeer0.quinella.httpClient
import okhttp3.Request

object RandomCat {

    private const val API_URL = "https://api.thecatapi.com/v1/images/search"

    private val jsonParser = JsonParser()

    fun generateRandomCat():String {
        val request = Request.Builder().url(API_URL).build()

        val response = httpClient.newCall(request).execute()
        val jsonResponse = jsonParser.parse(response.body()?.string()).asJsonArray

        return jsonResponse[0].asJsonObject.get("url").asString
    }

}