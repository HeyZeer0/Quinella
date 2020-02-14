package net.heyzeer0.quinella.core

import net.dv8tion.jda.api.entities.*
import net.heyzeer0.quinella.core.configs.coreConfig
import net.heyzeer0.quinella.core.managers.AsyncExecutor
import net.heyzeer0.quinella.core.managers.SecondExecutor
import net.heyzeer0.quinella.core.managers.containers.TaskContainer
import net.heyzeer0.quinella.database.objects.GuildProfile
import net.heyzeer0.quinella.database.objects.ServerProfile
import net.heyzeer0.quinella.database.objects.UserProfile
import net.heyzeer0.quinella.databaseManager
import net.heyzeer0.quinella.jda
import java.awt.Color
import java.util.*
import java.util.regex.Pattern
import kotlin.random.Random

fun currentTimeMillis():Long {
    return System.currentTimeMillis()
}

fun isNumber(input: String): Boolean {
    return try {
        Integer.parseInt(input)
        true
    } catch (e: Exception) { false }
}

fun randomGame(): String? {
    return coreConfig?.games?.get(Random.nextInt(coreConfig.games.size))
}

fun getServer(): ServerProfile {
    return databaseManager.getServerProfile()
}

fun runTask(runnable: () -> Unit, name: String = "Not Assigned"): TaskContainer {
    return SecondExecutor.registerTask(runnable, name)
}

fun runAsync(runnable: () -> Unit) {
    AsyncExecutor.runAsync(runnable)
}

fun getTextChannel(id: Long): TextChannel? {
    return jda.getTextChannelById(id)
}

fun getTextChannel(id: String): TextChannel? {
    return jda.getTextChannelById(id)
}

fun TextChannel.getMessage(id: Long): Message {
    return retrieveMessageById(id).complete()
}

fun User.getUserProfile():UserProfile {
    return databaseManager.getUserProfile(this.idLong)
}

fun Guild.getGuildProfile():GuildProfile {
    return databaseManager.getGuildProfile(this.idLong)
}

fun Member.isPlaying(gameName: String): Boolean {
    return activities.any { it.name == gameName }
}

fun Long.toReadableTime():String {
    val years: Long = this / 31104000000
    val months = this / 2592000000L % 12
    val days = this / 86400000L % 30
    val hours  = this / 3600000L % 24
    val minutes = this / 60000L % 60
    val seconds = this / 1000L % 60

    var time = (if (years == 0L) "" else "$years Anos, ") + (if (months == 0L) "" else "$months Mêses, ") + (if (days == 0L) "" else "$days Dias, ") + (if (hours == 0L) "" else "$hours Horas, ") + (if (minutes == 0L) "" else "$minutes Minutos, ")  + (if (seconds == 0L) "" else "$seconds Segundos, ")
    time = replaceLast(time, ", ", "")
    time = replaceLast(time, ",", " e")

    return time
}

fun replaceLast(text: String, regex: String, replacement: String): String {
    return text.replaceFirst("(?s)(.*)$regex".toRegex(), "$1$replacement")
}

private val HEX_REGEX = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$")
private val RGB_REGEX = Pattern.compile("(\\d{1,3}), (\\d{1,3}), (\\d{1,3})")

fun String.toColor():Color? {
    if(HEX_REGEX.matcher(this).matches()) {
        return Color(
            Integer.valueOf(substring(1, 3), 16),
            Integer.valueOf(substring(3, 5), 16),
            Integer.valueOf(substring(5, 7), 16)
        )
    }

    val matcher = RGB_REGEX.matcher(this)
    if (matcher.matches()) {
        return Color(
            Integer.valueOf(matcher.group(1)),
            Integer.valueOf(matcher.group(2)),
            Integer.valueOf(matcher.group(3)))
    }

    return null
}

fun Int.toBoolean(): Boolean {
    return this == 1
}

fun Color.toHexString():String {
    return String.format("#%02x%02x%02x", this.red, this.green, this.blue)
}

fun Calendar.getHours(): Int {
    return get(Calendar.HOUR_OF_DAY)
}

fun Calendar.getMinutes(): Int {
    return get(Calendar.MINUTE)
}

fun Calendar.getDay(): Int {
    return get(Calendar.DAY_OF_WEEK)
}

fun Calendar.setHours(amount: Int) {
    set(Calendar.HOUR_OF_DAY, amount)
}

fun Calendar.setMinutes(amount: Int) {
    set(Calendar.MINUTE, amount)
}

fun Calendar.toReadableTime(): String {
    return (timeInMillis - currentTimeMillis()).toReadableTime()
}

fun Calendar.currentDifference(): Long {
    return (timeInMillis - currentTimeMillis())
}