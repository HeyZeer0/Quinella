package net.heyzeer0.quinella.features.managers

import net.dv8tion.jda.api.entities.Message
import net.heyzeer0.quinella.core.*
import net.heyzeer0.quinella.core.enums.ActionResult
import net.heyzeer0.quinella.features.parsers.BlackDesert
import java.util.*
import kotlin.math.floor

object BlackDesertManager {

    private var currentStatus = mutableListOf(false, false, false)
    private var toDelete = mutableListOf<Message>()
    private var currentBoss = -1
    private var today = false

    fun updateChannelStatus(id: String): ActionResult {
        val server = getServer()

        if(server.blackDesertChannels.contains(id)) {
            server.blackDesertChannels.remove(id)

            server.asyncSave()
            return ActionResult.REMOVED
        }

        server.blackDesertChannels.add(id)
        server.asyncSave()
        return ActionResult.ADDED
    }

    fun registerAlgorithm() {
        runTask {
            startAlgorithm()
        }
    }

    private fun startAlgorithm() {
        sendTodayMessage()

        if(currentBoss == -1) {
            currentBoss = BlackDesert.getNextBossId()
            return
        }

        val nextBoss = BlackDesert.getNextBossId()
        if(currentBoss != nextBoss) {
            sendMessage(true)

            currentBoss = nextBoss
            currentStatus = mutableListOf(false, false, false)
            return
        }

        val bossDate = BlackDesert.getBossAsDate(currentBoss)
        val difference = bossDate.currentDifference()

        val diffHours = floor((difference / 3600000 % 24).toDouble()).toInt()
        val diffMinutes = floor((difference / 60000 % 60).toDouble()).toInt()

        var shouldSend = false

        if(diffHours == 0 && diffMinutes <= 5 && !currentStatus[2]) {
            shouldSend = true
            currentStatus[2] = true
        }else if(diffHours == 0 && diffMinutes <= 30 && !currentStatus[1]) {
            shouldSend = true
            currentStatus[1] = true
        }else if((diffHours == 1 || diffMinutes <= 60) && !currentStatus[0]) {
            shouldSend = true
            currentStatus[0] = true
        }

        if(!shouldSend) return

        sendMessage(false)
    }

    private fun sendMessage(spawned: Boolean) {
        clearMessages()

        val bossName = BlackDesert.getBossById(currentBoss) ?: return
        val timeLeft = BlackDesert.getBossAsDate(currentBoss).toReadableTime()

        val message = if(spawned) {
            if(bossName.contains("&")) {
                ":game_die: Os bosses ``$bossName`` irão spawnar em instantes!"
            }else{
                ":game_die: O boss ``$bossName`` irá spawnar em instantes!"
            }
        }else{
            if(bossName.contains("&")) {
                ":game_die: Os bosses ``$bossName`` irão spawnar em ``$timeLeft``"
            }else{
                ":game_die: O boss ``$bossName`` irá spawnar em ``$timeLeft``"
            }
        }

        sendMessage(message, !spawned)
    }

    private fun sendTodayMessage() {
        val currentDate = Calendar.getInstance(TimeZone.getTimeZone("BET"))

        if(currentDate.getHours() != 0) {
            today = false
            return
        }
        if(today) return
        today = true

        sendMessage(":game_die: Os bosses de hoje serão:\n``${BlackDesert.getTodayBosses()}``")
    }

    private fun sendMessage(message: String, delete: Boolean = true) {
        val server = getServer()
        val it = server.blackDesertChannels.iterator()

        while(it.hasNext()) {
            val channel = getTextChannel(it.next())
            if(channel == null) {
                it.remove()
                continue
            }

            channel.sendMessage(message).queue { if(delete) toDelete.add(it) }
        }
    }

    private fun clearMessages() {
        toDelete.forEach { c -> c.delete().queue() }
        toDelete.clear()
    }

}