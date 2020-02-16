package net.heyzeer0.quinella.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.heyzeer0.quinella.core.commands.CommandManager.commandList
import net.heyzeer0.quinella.core.commands.annotations.Argument
import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.containers.HelpContainer
import net.heyzeer0.quinella.core.commands.enums.ArgumentType
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.configs.coreConfig
import net.heyzeer0.quinella.core.enums.Emoji
import java.awt.Color

class HelpCommands {

    @Command(name = "help", type = CommandType.INFORMATIVE, args =[
        Argument("cmd", ArgumentType.STRING, "Gets more information about the provided command")
    ], description = "Lists all commands")
    fun help(e: MessageTranslator, args: ArgumentTranslator) {
        if (args.has("cmd")) {
            var command = args.getAsString("cmd")!!
            command = if (command.contains("/")) command.split("/")[0] else command

            if (commandList.none { it.annotation.name == command }) {
                e.sendMessage(Emoji.QUINELLA_THINK + "The provided command doesn't exists!")
                return
            }

            val foundCmd = commandList.firstOrNull{ it.annotation.name == command}
            val subcmds = commandList.filter { it.annotation.name.startsWith("$command/") }

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
            embedBuilder.setAuthor("Information about the command $command", null, "https://i.imgur.com/06PexZc.png")
            embedBuilder.setThumbnail("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/white-question-mark-ornament_2754.png")
            embedBuilder.setDescription("Listing help for command $command")

            embedBuilder.addField("Arguments:", containerMessage(helpContainer), true)

            e.sendMessage(embedBuilder)
            return
        }

        val funCmds = commandList.filter { it.annotation.type == CommandType.FUN && !it.annotation.name.contains("/") && it.annotation.showHelp }.map { it.annotation.name }.toList()
        val misCmds = commandList.filter { it.annotation.type == CommandType.MISCELLANEOUS && !it.annotation.name.contains("/") && it.annotation.showHelp }.map { it.annotation.name }.toList()
        val infoCmds = commandList.filter { it.annotation.type == CommandType.INFORMATIVE && !it.annotation.name.contains("/") && it.annotation.showHelp }.map { it.annotation.name }.toList()
        val modCmds = commandList.filter { it.annotation.type == CommandType.MODERATION && !it.annotation.name.contains("/") && it.annotation.showHelp }.map { it.annotation.name }.toList()

        val embedBuilder = EmbedBuilder()
        embedBuilder.addField(Emoji.COOKIE + "| Fun", checkIfNull(funCmds), false)
        embedBuilder.addField(Emoji.BOOKMARK + "| Information", checkIfNull(infoCmds), false)
        embedBuilder.addField(Emoji.DIAMOND + "| Miscellany", checkIfNull(misCmds), false)
        embedBuilder.addField(Emoji.LOCK + "| Moderation", checkIfNull(modCmds), false)

        embedBuilder.setColor(Color.MAGENTA)
        embedBuilder.setDescription("For more information type ``${coreConfig?.mainPrefix}help -cmd {command}``")
        embedBuilder.setFooter("Available Commands: ${commandList.size}", null)
        embedBuilder.setThumbnail("https://emojipedia-us.s3.amazonaws.com/thumbs/120/twitter/139/white-question-mark-ornament_2754.png")
        embedBuilder.setAuthor("Listing all available commands", null, "https://i.imgur.com/06PexZc.png")

        e.sendMessage(embedBuilder)
    }

    private fun checkIfNull(list: List<String>):String {
        return if (list.isEmpty()) "``There's no commands in this category!``"
        else "``" + list.joinToString(separator = "``, ``") + "``"
    }

    private fun containerMessage(container: HelpContainer): String {
        var message = "**${container.pathName}**: ${container.pathDescription}";
        if (container.arguments.isNotEmpty()) message += "\n${Emoji.EMPTY}${Emoji.EMPTY}"
        message += container.arguments.joinToString("\n${Emoji.EMPTY}${Emoji.EMPTY}")

        if (container.parents.isNotEmpty()) {
            container.parents.forEach { (_, u) -> message += "\n${Emoji.QUINELLA_TREE}" + containerMessage(u)}
        }

        return message;
    }

}