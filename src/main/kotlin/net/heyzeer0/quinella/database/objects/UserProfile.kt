package net.heyzeer0.quinella.database.objects

import net.heyzeer0.quinella.database.interfaces.RethinkObject

data class UserProfile(

    override val id: Long,

    //real objects

    override val table: String = "users"

): RethinkObject