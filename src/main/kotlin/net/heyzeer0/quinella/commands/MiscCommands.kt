package net.heyzeer0.quinella.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.heyzeer0.quinella.core.commands.annotations.Argument
import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.enums.ArgumentType
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.enums.Emoji
import net.heyzeer0.quinella.core.toHexString
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

class MiscCommands {

    @Command(name = "avatar", type = CommandType.MISCELLANEOUS, args = [
        Argument("u", ArgumentType.USER, "The user that you want to get the Avatar", optional = false)
    ], description = "See an user avatar!")
    fun avatar(e: MessageTranslator, args: ArgumentTranslator) {
        val user = args.getAsUsers("u")!![0]

        val embedBuilder = EmbedBuilder()
        embedBuilder.setColor(Color.GREEN)
        embedBuilder.setTitle(Emoji.CORRECT + "Avatar " + user.name)
        embedBuilder.setImage(user.avatarUrl)

        e.sendMessage(embedBuilder)
    }

    @Command(name = "color", type = CommandType.MISCELLANEOUS, args = [
        Argument("c", ArgumentType.COLOR, description = "The color that you want to visualize", optional = false)
    ], description = "See a preview of the provided color!")
    fun color(e: MessageTranslator, args: ArgumentTranslator) {
        val color = args.getAsColor("c")

        val baseImage = BufferedImage(100, 100, 2)
        val graphics2D: Graphics2D = baseImage.createGraphics()

        graphics2D.color = color
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        graphics2D.fillRect(0, 0, 100, 50)
        graphics2D.font = graphics2D.font.deriveFont(40f)
        graphics2D.drawString("Test", 10, 90)
        graphics2D.dispose()

        val embedBuilder = EmbedBuilder()
        embedBuilder.setTitle(Emoji.ANIM_CORRECT + "Here's the color")
        embedBuilder.setDescription("hex: ``" + color?.toHexString() + "``, rgb: ``" + color?.red + ", " + color?.green + ", " + color?.blue + "``")
        embedBuilder.setColor(color)

        e.sendImage(baseImage, embedBuilder)
    }

}