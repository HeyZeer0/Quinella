package net.heyzeer0.quinella.commands

import com.google.gson.GsonBuilder
import net.heyzeer0.quinella.core.commands.annotations.Argument
import net.heyzeer0.quinella.core.commands.annotations.Command
import net.heyzeer0.quinella.core.commands.enums.ArgumentType
import net.heyzeer0.quinella.core.commands.enums.CommandType
import net.heyzeer0.quinella.core.commands.translators.ArgumentTranslator
import net.heyzeer0.quinella.core.commands.translators.MessageTranslator
import net.heyzeer0.quinella.core.currentTimeMillis
import net.heyzeer0.quinella.core.enums.Emoji
import net.heyzeer0.quinella.features.managers.OsuManager
import net.heyzeer0.quinella.features.parsers.OsuParser
import java.util.function.Consumer

class TestCommands {

    @Command(name = "ping", permissions = [], type = CommandType.INFORMATIVE, description = "Returns the current bot ping")
    fun ping(e: MessageTranslator, args: ArgumentTranslator) {
        val ms = currentTimeMillis()
        e.sendMessage(Emoji.THINKING + "Calculating ping wait a moment!", Consumer { t ->
            t.editMessage(Emoji.CORRECT + "Pong! Currently ping is ``" + (currentTimeMillis() - ms) + "ms``").queue()
        })
    }

    @Command(name = "teste", permissions = [], type = CommandType.INFORMATIVE, description = "Test Command", botOwnerOnly = true)
    fun test(e: MessageTranslator, args: ArgumentTranslator) {
        e.sendMessage("tested!")
    }

    @Command(name = "teste/osu/pp", permissions = [], type = CommandType.INFORMATIVE, description = "Test Command", botOwnerOnly = true,
        args = [ Argument("id", ArgumentType.STRING, "beatmap id", false)])
    fun testosupp(e: MessageTranslator, args: ArgumentTranslator) {

        val gson = GsonBuilder().setPrettyPrinting().create()
        e.sendMessage(Emoji.QUINELLA_THINK + " Waiting for results", Consumer {
            OsuManager.queueAnalyse(args.getAsString("id")!!, { c ->
                it.editMessage("```json\n" + gson.toJson(c) + "\n```").queue()
                OsuManager.queueFullMapScan(args.getAsString("id")!!)
            })
        })
    }

    @Command(name = "teste/osu/beatmap", permissions = [], type = CommandType.INFORMATIVE, description = "Test Command", botOwnerOnly = true,
        args = [ Argument("id", ArgumentType.STRING, "beatmap id", false)])
    fun testosubeatmap(e: MessageTranslator, args: ArgumentTranslator) {
        val map = OsuParser.requestBeatMapProfile(args.getAsString("id")!!)
        if (map == null) {
            e.sendMessage("the provided map is invalid")
            return
        }

        val gson = GsonBuilder().setPrettyPrinting().create()
        e.sendMessage("```json\n" + gson.toJson(map) + "\n```")
    }

    @Command(name = "teste/osu/analise", permissions = [], type = CommandType.INFORMATIVE, description = "Test Command", botOwnerOnly = true,
        args = [ Argument("u", ArgumentType.STRING, "user", false)])
    fun testosuanalise(e: MessageTranslator, args: ArgumentTranslator) {
        val userBest = OsuParser.requestUserBests(args.getAsString("u")!!, 50)
        val userRecent = OsuParser.requestUserRecent(args.getAsString("u")!!, 50)

        println("[*] Full Scanning " + args.getAsString("u"))
        for (match in userBest) {
            OsuManager.queueFullMapScan(match.beatMapId)
        }

        for (match in userRecent) {
            OsuManager.queueFullMapScan(match.beatMapId)
        }

        e.sendMessage(Emoji.ANIM_CORRECT + "Started to analyse, see the command line to follow!")
    }


}