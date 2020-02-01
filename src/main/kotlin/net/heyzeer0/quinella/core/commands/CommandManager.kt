package net.heyzeer0.quinella.core.commands

import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.containers.CommandContainer
import net.heyzeer0.quinella.core.commands.enums.ArgumentType
import net.heyzeer0.quinella.core.commands.enums.Emoji
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.configs.coreConfig
import net.heyzeer0.quinella.core.isNumber
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.*

class CommandManager {

    var commandList = ArrayList<CommandContainer>()
    var commandAliases = HashMap<String, String>()

    fun registerCommands(clazz: KClass<*>) {
        val instance = clazz.createInstance()

        //register default commands
        clazz.functions.forEach {
            val ann = it.findAnnotation<Command>() ?: return@forEach
            if(it.parameters.size != 3) return@forEach

            val requiredParams = ann.args.filter { !it.optional }.map {
                "-" + it.name + " " + when(it.type) {
                    ArgumentType.STRING -> "{um texto}"
                    ArgumentType.NUMBER -> "{um número}"
                    ArgumentType.USER -> "{@alguem/id/nome}"
                    ArgumentType.ROLE -> "{@cargo/id/nome}"
                    ArgumentType.TEXT_CHANNEL -> "{#canal/id}"
                    ArgumentType.COLOR -> "{#hex/255, 255, 255}"
                }
            }

            ann.aliases.forEach { commandAliases[
                if(ann.name.contains("/")) ann.name.replace(ann.name.split("/")[0], it) else it
            ] = ann.name }

            commandList.plusAssign(CommandContainer(it, ann, instance, requiredParams))
        }
    }

    fun runCommand(e: GuildMessageReceivedEvent) {
        val messageRaw = e.message.contentRaw
        if(messageRaw.length <= 2 || !messageRaw.startsWith(coreConfig?.mainPrefix!!)) return

        val beheaded = messageRaw.replaceFirst(coreConfig.mainPrefix, "")
        val splitted = beheaded.split(" ") //!teste || splitted[0] = teste

        //command lookup
        var expectedCommand = ""
        for(arg in splitted) {
            if(arg.startsWith("-")) break

            expectedCommand += "$arg/"
        }

        expectedCommand = expectedCommand.substring(0, expectedCommand.length - 1)

        val cmdContainer = if(commandAliases.containsKey(expectedCommand)) {
            commandList.firstOrNull { it.annotation.name.equals(commandAliases[expectedCommand], true)} ?: return
        }else {
            commandList.firstOrNull { it.annotation.name.equals(expectedCommand, true)} ?: return
        }

        if(cmdContainer.annotation.sendTyping) e.channel.sendTyping().complete()

        val msgTranslator = MessageTranslator(e)
        if(!e.member!!.hasPermission(*cmdContainer.annotation.permissions)) {
            msgTranslator.sendMessage(Emoji.CRYING + "Você não possui permissão para executar este comando! ``" + cmdContainer.annotation.permissions.joinToString(separator = ", ") + "``")
            return
        }

        val argTranslator = ArgumentTranslator(
            beheaded.substring(cmdContainer.annotation.name.length),
            e.guild
        )

        var wrongArgs = emptyList<String>()
        for(arg in cmdContainer.annotation.args) {
            if(!argTranslator.has(arg.name) && !arg.optional) { //if any obligatory parameter is missing
                msgTranslator.sendMessage(
                    Emoji.CRYING + "Você esqueceu de definir os parametros obrigatórios!" +
                    "\n**Use:** ``${coreConfig.mainPrefix}${cmdContainer.annotation.name.replace("/", " ")} " + cmdContainer.requiredParams.joinToString(" ") + "``")
                return
            }

            val invalid = when(arg.type) {
                ArgumentType.STRING -> false
                ArgumentType.NUMBER -> !isNumber(argTranslator.getAsString(arg.name)!!)
                ArgumentType.USER -> argTranslator.getAsUsers(arg.name)!!.isEmpty()
                ArgumentType.ROLE -> argTranslator.getAsRoles(arg.name)!!.isEmpty()
                ArgumentType.TEXT_CHANNEL -> argTranslator.getAsTextChannels(arg.name)!!.isEmpty()
                ArgumentType.COLOR -> argTranslator.getAsColor(arg.name) == null
            }
            if(!invalid) continue

            wrongArgs = wrongArgs + cmdContainer.requiredParams.first{ it.startsWith("-" + arg.name) }
        }

        if(wrongArgs.isNotEmpty()) {
            msgTranslator.sendMessage(
                Emoji.CRYING + "Os seguintes parametros definidos estão invalidos:\n" +
                    "``" + wrongArgs.joinToString("\n") + "``")
            return
        }

        try{
            cmdContainer.method.call(cmdContainer.instance, msgTranslator, argTranslator)
        }catch (ex: Exception) {
            //TODO redirect the stacktrace to somewhere and alert the instance owner
            ex.printStackTrace()
        }
    }

}