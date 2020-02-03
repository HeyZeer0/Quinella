package net.heyzeer0.quinella.core.utilities

import net.heyzeer0.quinella.getJDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

val DISCORD_ID = Pattern.compile("\\d{17,20}")
val FULL_USER_REF = Pattern.compile("(\\S.{0,30}\\S)\\s*#(\\d{4})")
val USER_MENTION = Pattern.compile("<@!?(\\d{17,20})>")
val CHANNEL_MENTION = Pattern.compile("<#(\\d{17,20})>")
val ROLE_MENTION = Pattern.compile("<@&(\\d{17,20})>")
val EMOTE_MENTION = Pattern.compile("<:(.{2,32}):(\\d{17,20})>")

fun findUsers(query: String):List<User> {
    val userMention = USER_MENTION.matcher(query)
    val fullRefMatch = FULL_USER_REF.matcher(query)

    if (userMention.matches()) {
        return Collections.singletonList(getJDA().getUserById(userMention.group(1))!!)
    } else if (fullRefMatch.matches()) {
        val lowerName = fullRefMatch.group(1).toLowerCase()
        val discriminator = fullRefMatch.group(2)

        val users = getJDA().userCache.stream().filter { it.name.toLowerCase() == lowerName && it.discriminator == discriminator }.collect(Collectors.toList())

        if (users.isNotEmpty()) return users
    } else if (DISCORD_ID.matcher(query).matches()) {
        val user = getJDA().getUserById(query);
        if (user != null) return Collections.singletonList(user)
    }

    val exact = ArrayList<User>()
    val wrongCase = ArrayList<User>()
    val startsWith = ArrayList<User>()
    val contains = ArrayList<User>()

    val lowerQuery = query.toLowerCase()
    getJDA().userCache.forEach {
        val name = it.name
        if (name == query) exact.add(it)
        else if (name.equals(query, true) && exact.isEmpty()) wrongCase.add(it)
        else if (name.toLowerCase().startsWith(lowerQuery) && wrongCase.isEmpty()) startsWith.add(it)
        else if (name.toLowerCase().contains(lowerQuery) && startsWith.isEmpty()) contains.add(it)
    }

    if (exact.isNotEmpty()) return Collections.unmodifiableList(exact)
    if (wrongCase.isNotEmpty()) return Collections.unmodifiableList(wrongCase)
    if (startsWith.isNotEmpty()) return Collections.unmodifiableList(startsWith)

    return Collections.unmodifiableList(contains)
}

fun findTextChannels(query: String, guild: Guild? = null):List<TextChannel> {
    val channelMention = CHANNEL_MENTION.matcher(query)

    if (channelMention.matches()) {
        return Collections.singletonList(if (guild == null) getJDA().getTextChannelById(channelMention.group(1))!! else guild.getTextChannelById(channelMention.group(1))!!)
    }else if (DISCORD_ID.matcher(query).matches()) {
        return Collections.singletonList(if (guild == null) getJDA().getTextChannelById(query)!! else guild.getTextChannelById(query)!!)
    }

    return emptyList()
}

fun findRoles(query: String, guild: Guild):List<Role> {
    val roleMention = ROLE_MENTION.matcher(query)
    if (roleMention.matches()) {
        val role = guild.getRoleById(roleMention.group(1))
        if (role != null && role.isMentionable) return Collections.singletonList(role)
    }else if (DISCORD_ID.matcher(query).matches()) {
        val role = guild.getRoleById(query)
        if (role != null) return Collections.singletonList(role)
    }

    val exact = ArrayList<Role>()
    val wrongCase = ArrayList<Role>()
    val startsWith = ArrayList<Role>()
    val contains = ArrayList<Role>()

    val lowerQuery = query.toLowerCase()
    getJDA().roleCache.forEach {
        val name = it.name
        if (name == query) exact.add(it)
        else if (name.equals(query, true) && exact.isEmpty()) wrongCase.add(it)
        else if (name.toLowerCase().startsWith(lowerQuery) && wrongCase.isEmpty()) startsWith.add(it)
        else if (name.toLowerCase().contains(lowerQuery) && startsWith.isEmpty()) contains.add(it)
    }

    if (exact.isNotEmpty()) return Collections.unmodifiableList(exact)
    if (wrongCase.isNotEmpty()) return Collections.unmodifiableList(wrongCase)
    if (startsWith.isNotEmpty()) return Collections.unmodifiableList(startsWith)

    return Collections.unmodifiableList(contains)
}