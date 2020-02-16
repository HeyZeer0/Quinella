package net.heyzeer0.quinella.core.listeners.handlers

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.heyzeer0.quinella.core.commands.CommandManager.runCommand
import net.heyzeer0.quinella.core.managers.SecondExecutor

object GuildMessageHandler {

    fun onGuildMessageReceived(e: GuildMessageReceivedEvent) {
        runCommand(e)

        SecondExecutor.verifyTask() //verifies if the task didn't die for a random reason
    }
    
}