package net.heyzeer0.quinella.features.containers

import net.heyzeer0.quinella.features.instances.OsuOppaiAnalyse

class OppaiQueueContainer(

    val mapId: String,
    var onFinish: ((OsuOppaiAnalyse?) -> Unit)?,

    val mods: Int = 0,
    val combo: Int = -1,
    val hit300: Int = -1,
    val hit100: Int = 0,
    val hit50: Int = 0,
    val misses: Int = 0

)