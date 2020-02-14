package net.heyzeer0.quinella.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.heyzeer0.quinella.core.commands.annotations.Argument
import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.enums.ArgumentType
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.enums.ActionResult
import net.heyzeer0.quinella.core.enums.Emoji
import net.heyzeer0.quinella.core.toReadableTime
import net.heyzeer0.quinella.features.managers.BlackDesertManager
import net.heyzeer0.quinella.features.managers.OsuManager
import net.heyzeer0.quinella.features.parsers.BlackDesert
import net.heyzeer0.quinella.features.parsers.RandomCat
import net.heyzeer0.quinella.features.parsers.RandomDog
import java.awt.Color
import java.util.function.Consumer

class FunCommands {

    @Command(name = "gato", type = CommandType.FUN, description = "Get a random cat!")
    fun randomCat(e: MessageTranslator, args: ArgumentTranslator) {
        val embedBuilder = EmbedBuilder()
        embedBuilder.setColor(Color.GREEN)
        embedBuilder.setImage(RandomCat.generateRandomCat())

        e.sendMessage(embedBuilder)
    }

    @Command(name = "cachorro", type = CommandType.FUN, description = "Get a random dog!")
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

        when (BlackDesertManager.updateChannelStatus(channel.id)) {
            ActionResult.REMOVED -> e.sendMessage("${Emoji.ANIM_CORRECT} Este canal não irá mais receber notificações do jogo!")
            ActionResult.ADDED -> e.sendMessage("${Emoji.ANIM_CORRECT} Este canal agora irá receber notificações do jogo!")
        }
    }

    @Command(name = "osu", type = CommandType.FUN, description = "Lists all available osu related commands!")
    fun osu(e: MessageTranslator, args: ArgumentTranslator) {

    }

    @Command(name = "osu/profile", type = CommandType.FUN, description = "See a osu player profile!",
        args = [ Argument(name = "u", type = ArgumentType.STRING, description = "The user name or id", optional = false) ])
    fun osuProfile(e: MessageTranslator, args: ArgumentTranslator) {
        e.sendMessage(Emoji.ANIM_LOADING + "Generating **${args.getAsString("u")}** profile...", Consumer {
            val image = OsuManager.drawUserProfileImage(args.getAsString("u")!!)

            if (image == null) {
                it.editMessage(Emoji.QUINELLA_THINK + "The provided user doesn't have an osu! account.").queue()
                return@Consumer
            }

            it.delete().queue()
            e.sendImage(image, null, args.getAsString("u")!!)
        })
    }

}