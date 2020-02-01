package net.heyzeer0.quinella.core.commands.containers

import net.heyzeer0.quinella.core.commands.annotations.Command
import kotlin.reflect.KFunction

data class CommandContainer(

    val method: KFunction<*>,
    val annotation: Command,
    val instance: Any,
    val requiredParams: List<String>

)