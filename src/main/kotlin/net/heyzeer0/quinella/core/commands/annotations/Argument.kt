package net.heyzeer0.quinella.core.commands.annotations

import net.heyzeer0.quinella.core.commands.enums.ArgumentType

annotation class Argument(

    val name: String,
    val type: ArgumentType,
    val description: String,
    val optional: Boolean = true

)