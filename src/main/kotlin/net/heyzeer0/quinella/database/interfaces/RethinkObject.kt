package net.heyzeer0.quinella.database.interfaces

import com.google.gson.Gson
import com.rethinkdb.RethinkDB.r
import net.heyzeer0.quinella.databaseManager

interface RethinkObject {

    val table: String
    val id: Any

    private fun save() {
        val map = Gson().fromJson<HashMap<*, *>>(Gson().toJson(this), HashMap<Any, Any>().javaClass)
        r.table(table).insert(map).optArg("conflict", "replace").runNoReply(databaseManager.connection)
    }

    private fun delete() {
        r.table(table).get(id).delete().runNoReply(databaseManager.connection)
    }

    fun asyncSave() {
        databaseManager.executor.submit { save() }
    }

    fun asyncDelete() {
        databaseManager.executor.submit { delete() }
    }

}