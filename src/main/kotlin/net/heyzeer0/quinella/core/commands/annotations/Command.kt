package net.heyzeer0.quinella.core.commands.annotations

import net.dv8tion.jda.api.Permission
import net.heyzeer0.quinella.core.commands.enums.CommandType

annotation class Command(

    val name: String,
    val permissions: Array<Permission> = [],
    val type: CommandType,
    val args: Array<Argument> = [],
    val description: String,
    val sendTyping: Boolean = true,
    val botOwnerOnly: Boolean = false,
    val showHelp: Boolean = true,
    val aliases: Array<String> = []

)