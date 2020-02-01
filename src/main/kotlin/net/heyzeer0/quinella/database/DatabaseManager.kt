package net.heyzeer0.quinella.database

import com.google.gson.Gson
import com.rethinkdb.RethinkDB.r
import net.heyzeer0.quinella.core.configs.databaseConfig
import net.heyzeer0.quinella.database.objects.GuildProfile
import net.heyzeer0.quinella.database.objects.ServerProfile
import net.heyzeer0.quinella.database.objects.UserProfile
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DatabaseManager {

    val connection = r.connection().hostname(databaseConfig!!.ip).port(databaseConfig.port).db(databaseConfig.database).user(databaseConfig.user, databaseConfig.password).connect()!!
    val executor: ExecutorService = Executors.newSingleThreadExecutor()

    val gson = Gson()

    fun getUserProfile(id: Long): UserProfile {
        val requestResult = r.table("users").get(id).run<HashMap<*, *>>(connection)
        if(requestResult != null) return gson.fromJson(gson.toJsonTree(requestResult), UserProfile::class.java)

        return UserProfile(id)
    }

    fun getGuildProfile(id: Long): GuildProfile {
        val requestResult = r.table("guilds").get(id).run<HashMap<*, *>>(connection)
        if(requestResult != null) return gson.fromJson(gson.toJsonTree(requestResult), GuildProfile::class.java)

        return GuildProfile(id)
    }

    fun getServerProfile(): ServerProfile {
        val requestResult = r.table("server").get("server").run<HashMap<*, *>>(connection)
        if(requestResult != null) return gson.fromJson(gson.toJsonTree(requestResult), ServerProfile::class.java)

        return ServerProfile()
    }

}