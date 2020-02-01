package net.heyzeer0.quinella.core.commands.annotations

import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.dv8tion.jda.api.Permission

annotation class Command(

    val name: String,
    val permissions: Array<Permission> = [],
    val type: CommandType,
    val args: Array<Argument> = [],
    val description: String,
    val sendTyping: Boolean = true,
    val botOwnerOnly: Boolean = false,
    val aliases: Array<String> = []

)