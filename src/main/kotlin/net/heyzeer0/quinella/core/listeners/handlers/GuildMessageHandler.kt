package net.heyzeer0.quinella.core.listeners.handlers

import net.heyzeer0.quinella.commandManager
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.heyzeer0.quinella.core.managers.SecondExecutor

object GuildMessageHandler {

    fun onGuildMessageReceived(e: GuildMessageReceivedEvent) {
        commandManager.runCommand(e)

        SecondExecutor.verifyTask() //verifies if the task didn't die for a random reason
    }
    
}