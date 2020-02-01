package net.heyzeer0.quinella.commands

import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.features.parsers.RandomCat
import net.heyzeer0.quinella.features.parsers.RandomDog
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.heyzeer0.quinella.core.commands.enums.Emoji
import net.heyzeer0.quinella.core.enums.ActionResult
import net.heyzeer0.quinella.core.toReadableTime
import net.heyzeer0.quinella.features.managers.BlackDesertManager
import net.heyzeer0.quinella.features.parsers.BlackDesert
import java.awt.Color

class FunCommands {

    @Command(name = "gato", type = CommandType.FUN, description = "Receba um gato aleatório!")
    fun randomCat(e: MessageTranslator, args: ArgumentTranslator) {
        val embedBuilder = EmbedBuilder()
        embedBuilder.setColor(Color.GREEN)
        embedBuilder.setImage(RandomCat.generateRandomCat())

        e.sendMessage(embedBuilder)
    }

    @Command(name = "cachorro", type = CommandType.FUN, description = "Receba um cachorro aleatoório!")
    fun randomDog(e: MessageTranslator, args: ArgumentTranslator) {
        val embedBuilder = EmbedBuilder()
        embedBuilder.setColor(Color.GREEN)
        embedBuilder.setImage(RandomDog.generateRandomDog())

        e.sendMessage(embedBuilder)
    }

    @Command(name = "blackdesert", aliases = ["bdo"], type = CommandType.FUN, description = "Veja informações sobre os bosses de hoje!")
    fun blackDesert(e: MessageTranslator, args: ArgumentTranslator) {
        val bossId = BlackDesert.getNextBossId()

        e.sendMessage(":game_die: Os boses de hoje são: ``${BlackDesert.getTodayBosses()}``")
        e.sendMessage("**Proximo boss:** ``${BlackDesert.getBossById(bossId)}`` em ``${BlackDesert.getBossAsDate(bossId).toReadableTime()}``")
    }

    @Command(name = "blackdesert/register", aliases = ["bdo"], type = CommandType.FUN, permissions = [Permission.MANAGE_CHANNEL], description = "Registre seu canal para receber notificações")
    fun blackDesertRegister(e: MessageTranslator, args: ArgumentTranslator) {
        val channel = e.getChannel()

        when(BlackDesertManager.updateChannelStatus(channel.id)) {
            ActionResult.REMOVED -> e.sendMessage("${Emoji.ANIM_CORRECT} Este canal não irá mais receber notificações do jogo!")
            ActionResult.ADDED -> e.sendMessage("${Emoji.ANIM_CORRECT} Este canal agora irá receber notificações do jogo!")
        }
    }

}