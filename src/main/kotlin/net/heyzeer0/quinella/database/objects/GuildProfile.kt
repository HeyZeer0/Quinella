package net.heyzeer0.quinella.database.objects

import net.heyzeer0.quinella.database.interfaces.RethinkObject

data class GuildProfile(

    override val id: Long,

    //real objects
    //TODO stuff

    override val table: String = "guilds"

): RethinkObject