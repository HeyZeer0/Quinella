package net.heyzeer0.quinella.core.commands.translators

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.heyzeer0.quinella.core.toColor
import net.heyzeer0.quinella.core.utilities.findRoles
import net.heyzeer0.quinella.core.utilities.findTextChannels
import net.heyzeer0.quinella.core.utilities.findUsers
import java.awt.Color
import java.util.regex.Pattern

class ArgumentTranslator(rawArguments: String, val guild: Guild) {

    private val ARGUMENT_REGEX = Pattern.compile("(?:-{1,2}|/)(\\w+)(?:[=:]?|\\s+)([^-\\s\"][^\"]*?|\"[^\"]*\")?(?=\\s+[-/]|\$)")

    //-arg1 msg -arg2 msg2...
    private var mappedArguments = emptyMap<String, String>()

    init {
        if(!rawArguments.isEmpty() && rawArguments.contains(" ")) {
            val matcher = ARGUMENT_REGEX.matcher(rawArguments)

            while(matcher.find()) {
                if(matcher.groupCount() < 2) continue

                mappedArguments = mappedArguments + Pair(matcher.group(1), matcher.group(2))
            }
        }
    }

    fun length(): Int {
        return mappedArguments.size
    }

    fun has(key: String): Boolean {
        return mappedArguments.containsKey(key)
    }

    fun getAsString(key: String): String? {
        return mappedArguments[key]
    }

    fun getAsUsers(key: String): List<User>? {
        return mappedArguments[key]?.let { findUsers(it) }
    }

    fun getAsColor(key: String): Color? {
        return mappedArguments[key]?.toColor()
    }

    fun getAsRoles(key: String): List<Role>? {
        return mappedArguments[key]?.let { findRoles(it, guild) }
    }

    fun getAsTextChannels(key: String): List<TextChannel>? {
        return mappedArguments[key]?.let { findTextChannels(it, guild) }
    }

    override fun toString():String {
        return mappedArguments.toString()
    }

    private fun removeBlank(msg: String):String {
        var after = msg
        if(msg.startsWith(" ")) after = after.substring(1)
        if(msg.endsWith(" ")) after = after.substring(0, after.length - 1)

        return after
    }

}