package net.heyzeer0.quinella.core.listeners

import net.heyzeer0.quinella.core.listeners.handlers.GuildMessageHandler
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.heyzeer0.quinella.core.managers.SecondExecutor

class MainListener:EventListener {

    override fun onEvent(event: GenericEvent) {
        if(event is GuildMessageReceivedEvent) {
            GuildMessageHandler.onGuildMessageReceived(event)
        }
    }

}