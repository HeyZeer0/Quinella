package net.heyzeer0.quinella.core.configs.instances

import net.heyzeer0.quinella.core.configs.annotations.Settings

@Settings(name = "core-config")
class CoreConfig {

    val botToken: String = "<insert-here>"
    val mainPrefix: String = "!"
    val games: List<String> = listOf("with System Commands", "with your Fluctlight", "with your memories", "with your love", "with humans")

}