package net.heyzeer0.quinella.core.configs.instancies

import net.heyzeer0.quinella.core.configs.annotations.Settings

@Settings(name = "database-config")
class DatabaseConfig {

    val ip = "localhost"
    val port = 3333
    val database = "quinella"
    val user = "quinella"
    val password = "quinella123"

}