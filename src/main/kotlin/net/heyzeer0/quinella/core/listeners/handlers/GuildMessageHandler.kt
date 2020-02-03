package net.heyzeer0.quinella.core.listeners.handlers

import net.heyzeer0.quinella.commandManager
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

object GuildMessageHandler {

    fun onGuildMessageReceived(e: GuildMessageReceivedEvent) {
        commandManager.runCommand(e)
    }
    
}