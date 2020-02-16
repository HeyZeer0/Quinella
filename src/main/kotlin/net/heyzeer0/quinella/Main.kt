package net.heyzeer0.quinella

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.heyzeer0.quinella.commands.*
import net.heyzeer0.quinella.core.commands.CommandManager.registerCommands
import net.heyzeer0.quinella.core.configs.coreConfig
import net.heyzeer0.quinella.core.currentTimeMillis
import net.heyzeer0.quinella.core.listeners.MainListener
import net.heyzeer0.quinella.core.randomGame
import net.heyzeer0.quinella.database.DatabaseManager
import net.heyzeer0.quinella.features.FeaturesManager
import okhttp3.OkHttpClient
import java.io.File

lateinit var jda: JDA

lateinit var databaseManager: DatabaseManager
val httpClient = OkHttpClient()

private fun main() {
    if (coreConfig?.botToken.equals("<insert-here>")) {
        println("> Please set up a bot token before starting the bot!")
        return
    }

    databaseManager = DatabaseManager()

    //<editor-fold desc="Inicialização JDA">
    var ms = currentTimeMillis()
    println("> Initializing JDA...")
    jda = JDABuilder(coreConfig?.botToken)
        .setAutoReconnect(true)
        .addEventListeners(MainListener())
        .build()
    jda.awaitReady()
    jda.presence.activity = Activity.playing(randomGame()!!)
    println(" - Took " + (currentTimeMillis() -ms) + "ms to initialize")
    //</editor-fold>

    //<editor-fold desc="Comandos sendo registrados">
    ms = currentTimeMillis()
    println("> Registering all commands... ")
    registerCommands(TestCommands::class)
    registerCommands(HelpCommands::class)
    registerCommands(MiscCommands::class)
    registerCommands(FunCommands::class)
    registerCommands(ModCommands::class)
    println(" - Took " + (currentTimeMillis() -ms) + "ms to register all commands")
    //</editor-fold>

    //<editor-fold desc="Features sendo registradas">
    println("> Registering all features... ")
    ms = currentTimeMillis()
    FeaturesManager.registerFeatures()
    println(" - Took " + (currentTimeMillis() -ms) + "ms to register all features")
    //</editor-fold>
}

fun getDataFolder():File {
    return File(System.getProperty("user.dir"), "data")
}

fun getJDA():JDA {
    return jda
}