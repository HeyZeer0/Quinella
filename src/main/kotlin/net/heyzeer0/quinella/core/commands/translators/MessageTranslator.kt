package net.heyzeer0.quinella.core.commands.translators

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.heyzeer0.quinella.database.objects.GuildProfile
import net.heyzeer0.quinella.database.objects.UserProfile
import net.heyzeer0.quinella.databaseManager
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception
import java.util.function.Consumer
import javax.imageio.ImageIO

class MessageTranslator(val origin: GuildMessageReceivedEvent) {

    fun sendMessage(message: Message, consumer: Consumer<Message> = Consumer {}) {
        origin.channel.sendMessage(message).queue(consumer)
    }

    fun sendMessage(embed: EmbedBuilder, consumer: Consumer<Message> = Consumer {}) {
        sendMessage(MessageBuilder().setEmbed(embed.build()).build(), consumer)
    }

    fun sendMessage(message: String, consumer: Consumer<Message> = Consumer {}) {
        origin.channel.sendMessage(message).queue(consumer)
    }

    fun sendImage(image: BufferedImage, embedBuilder: EmbedBuilder? = null, fileName: String = "image", consumer: Consumer<Message> = Consumer {}) {
        val byteArray = ByteArrayOutputStream()
        try{
            ImageIO.write(image, "png", byteArray)
        }catch (e: Exception) {}

        val inputStream: InputStream = ByteArrayInputStream(byteArray.toByteArray())

        if(embedBuilder == null)
            origin.channel.sendFile(inputStream, "${fileName}.png").queue(consumer)
        else
            origin.channel.sendMessage(MessageBuilder().setEmbed(embedBuilder.setImage("attachment://${fileName}.png").build()).build())
                .addFile(inputStream, "${fileName}.png").queue(consumer)
    }

    fun getAuthor():User {
        return origin.author
    }

    fun getChannel():TextChannel {
        return origin.channel
    }

    fun getGuild():Guild {
        return origin.guild
    }

    fun getMember():Member {
        return origin.member!!
    }

    fun getChannelType():ChannelType {
        return origin.channel.type
    }

    fun deleteMessage() {
        origin.message.delete().queue({}, {})
    }

}
