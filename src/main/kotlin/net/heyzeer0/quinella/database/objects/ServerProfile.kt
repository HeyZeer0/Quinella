package net.heyzeer0.quinella.database.objects

import net.heyzeer0.quinella.database.interfaces.RethinkObject
import net.heyzeer0.quinella.features.instances.OsuOppaiAnalyse

data class ServerProfile(

    override val id: String = "server",

    //real objects
    var blackDesertChannels: HashSet<String> = hashSetOf(),

    var osuAnalysedBeatMaps: ArrayList<OsuOppaiAnalyse> = arrayListOf(),

    override val table: String = "server"

): RethinkObject