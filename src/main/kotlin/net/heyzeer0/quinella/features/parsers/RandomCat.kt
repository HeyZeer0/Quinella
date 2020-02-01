package net.heyzeer0.quinella.features.parsers

import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request

object RandomCat {

    const val apiUrl = "https://api.thecatapi.com/v1/images/search"
    private val client = OkHttpClient()
    private val jsonParser = JsonParser()

    fun generateRandomCat():String {
        val request = Request.Builder().url(apiUrl).build()

        val response = client.newCall(request).execute()
        val jsonResponse = jsonParser.parse(response.body()?.string()).asJsonArray

        return jsonResponse[0].asJsonObject.get("url").asString
    }

}