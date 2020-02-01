package net.heyzeer0.quinella.database.objects

import net.heyzeer0.quinella.database.interfaces.RethinkObject

data class ServerProfile(

    override val id: String = "server",

    //real objects
    var blackDesertChannels: HashSet<String> = hashSetOf(),

    override val table: String = "server"

): RethinkObject