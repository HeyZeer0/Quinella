package net.heyzeer0.quinella.commands

import net.heyzeer0.quinella.commandManager
import net.heyzeer0.quinella.core.commands.annotations.Argument
import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.enums.ArgumentType
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.enums.Emoji
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.configs.coreConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.heyzeer0.quinella.core.commands.containers.HelpContainer
import java.awt.Color

class HelpCommands {

    @Command(name = "help", type = CommandType.INFORMATIVE, args =[
        Argument("comando", ArgumentType.STRING, "Retorna mais informações sobre o comando definido")
    ], description = "Lista todos os comandos ou mostra ajuda sobre um comando específico")
    fun help(e: MessageTranslator, args: ArgumentTranslator) {
        if (args.has("comando")) {
            var command = args.getAsString("comando")!!
            command = if (command.contains("/")) command.split("/")[0] else command

            if (commandManager.commandList.none { it.annotation.name == command }) {
                e.sendMessage(Emoji.CRYING + "O comando definido não existe!")
                return
            }

            val foundCmd = commandManager.commandList.firstOrNull{ it.annotation.name == command}
            val subcmds = commandManager.commandList.filter { it.annotation.name.startsWith("$command/") }

            val helpContainer = HelpContainer(command, foundCmd!!.annotation.description)
            helpContainer.arguments = foundCmd.annotation.args.map { "-" + it.name + " = " + it.description }.toList()

            if (subcmds.isNotEmpty()) {
                for (input in subcmds) {
                    val parents = input.annotation.name.split("/")

                    var subContainer = helpContainer
                    for (i in 1 until parents.size) {
                        if (subContainer.parents[parents[i]] == null) {
                            subContainer.parents[parents[i]] = HelpContainer("", "")
                        }

                        subContainer = subContainer.parents[parents[i]]!!
                    }

                    subContainer.pathName = parents[parents.size-1]
                    subContainer.pathDescription = input.annotation.description

                    subContainer.arguments = input.annotation.args.map { "-" + it.name + " = " + it.description }.toList()
                }
            }

            val embedBuilder = EmbedBuilder()
            embedBuilder.setColor(Color.MAGENTA)
            embedBuilder.setAuthor("Informações sobre o comando $command", null, "https://i.imgur.com/06PexZc.png")
            embedBuilder.setThumbnail("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/white-question-mark-ornament_2754.png")
            embedBuilder.setDescription("Listando ajuda para o comando $command")

            embedBuilder.addField("Argumentos:", containerMessage(helpContainer), true)

            e.sendMessage(embedBuilder)
            return
        }

        val funCmds = commandManager.commandList.filter { it.annotation.type == CommandType.FUN }.filter{ !it.annotation.name.contains("/") }.map { it.annotation.name }.toList()
        val misCmds = commandManager.commandList.filter { it.annotation.type == CommandType.MISCELLANEOUS }.filter{ !it.annotation.name.contains("/") }.map { it.annotation.name }.toList()
        val infoCmds = commandManager.commandList.filter { it.annotation.type == CommandType.INFORMATIVE }.filter{ !it.annotation.name.contains("/") }.map { it.annotation.name }.toList()
        val modCmds = commandManager.commandList.filter { it.annotation.type == CommandType.MODERATION }.filter{ !it.annotation.name.contains("/") }.map { it.annotation.name }.toList()

        val embedBuilder = EmbedBuilder()
        embedBuilder.addField(Emoji.COOKIE + "| Diversão", checkIfNull(funCmds), false)
        embedBuilder.addField(Emoji.BOOKMARK + "| Informação", checkIfNull(infoCmds), false)
        embedBuilder.addField(Emoji.DIAMOND + "| Diversos", checkIfNull(misCmds), false)
        embedBuilder.addField(Emoji.LOCK + "| Moderação", checkIfNull(modCmds), false)

        embedBuilder.setColor(Color.MAGENTA)
        embedBuilder.setDescription("Para mais informações digite ``${coreConfig?.mainPrefix}help -comando {comando}``")
        embedBuilder.setFooter("Comandos disponívels: ${commandManager.commandList.size}", null)
        embedBuilder.setThumbnail("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/white-question-mark-ornament_2754.png")
        embedBuilder.setAuthor("Listando todos os comandos disponíveis", null, "https://i.imgur.com/06PexZc.png")

        e.sendMessage(embedBuilder)
    }

    private fun checkIfNull(list: List<String>):String {
        return if (list.isEmpty()) "``não há comandos nesta categoria!``"
        else "``" + list.joinToString(separator = "``, ``") + "``"
    }

    private fun containerMessage(container: HelpContainer): String {
        var message = "**${container.pathName}**: ${container.pathDescription}\n";
        message += container.arguments.joinToString("\n")

        if (container.parents.isNotEmpty()) {
            container.parents.forEach { (_, u) -> message += "\n<:invisible:621781910653370380>" + containerMessage(u).replace("\n", "\n<:invisible:621781910653370380>") }
        }

        return message;
    }

}