package net.heyzeer0.quinella.features.instances

class OsuMatchProfile(

    val beatMapId: String,
    val scoreId: String?,
    val playerId: String,

    val score: Int,
    val maxCombo: Int,
    val hit50: Int,
    val hit10: Int,
    val hit300: Int,
    val misses: Int,

    val perfect: Boolean,
    val enabledMods: Int,
    val rank: String,
    val performancePoints: Double?,

    val replayAvailable: Boolean?

)