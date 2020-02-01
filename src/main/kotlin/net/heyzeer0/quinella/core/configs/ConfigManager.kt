package net.heyzeer0.quinella.core.configs

import net.heyzeer0.quinella.core.configs.annotations.Settings
import net.heyzeer0.quinella.core.configs.instancies.CoreConfig
import net.heyzeer0.quinella.core.configs.instancies.DatabaseConfig
import net.heyzeer0.quinella.getDataFolder
import com.google.gson.GsonBuilder
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

private val configFolder = File(getDataFolder(), "config")
private val gson = GsonBuilder().setPrettyPrinting().create()

val coreConfig: CoreConfig? =
    loadConfig(CoreConfig::class)
val databaseConfig: DatabaseConfig? =
    loadConfig(DatabaseConfig::class)

private inline fun <reified T:Any> loadConfig(clazz: KClass<*>): T? {
    val ann = clazz.findAnnotation<Settings>() ?: return null

    configFolder.mkdirs()

    val configFile = File(configFolder, ann.name + ".config")
    if(!configFile.exists()) {
        val instance = clazz.createInstance() as T
        val json = gson.toJson(instance)

        configFile.writeText(json, StandardCharsets.UTF_8)
        return instance
    }

    return gson.fromJson(configFile.readText(StandardCharsets.UTF_8), T::class.java) as T
}