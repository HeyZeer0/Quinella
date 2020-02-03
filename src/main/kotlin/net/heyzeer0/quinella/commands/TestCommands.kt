package net.heyzeer0.quinella.commands

import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.enums.Emoji
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.currentTimeMillis
import net.heyzeer0.quinella.core.toReadableTime
import net.heyzeer0.quinella.features.parsers.BlackDesert
import java.util.function.Consumer

class TestCommands {

    @Command(name = "ping", permissions = [], type = CommandType.INFORMATIVE, description = "Returns the current bot ping")
    fun ping(e: MessageTranslator, args: ArgumentTranslator) {
        val ms = currentTimeMillis()
        e.sendMessage(Emoji.THINKING + "Calculating ping wait a moment!", Consumer { t ->
            t.editMessage(Emoji.CORRECT + "Pong! Currently ping is ``" + (currentTimeMillis() - ms) + "ms``").queue()
        })
    }

    @Command(name = "test", permissions = [], type = CommandType.INFORMATIVE, description = "Test Command", botOwnerOnly = true)
    fun test(e: MessageTranslator, args: ArgumentTranslator) {
        val bossId = BlackDesert.getNextBossId()
        e.sendMessage("``${BlackDesert.getTodayBosses()}``")
        e.sendMessage("**Proximo boss:** ``${BlackDesert.getBossById(bossId)}`` em ``${BlackDesert.getBossAsDate(bossId).toReadableTime()}``")
    }

}