package net.heyzeer0.quinella.features.instances

class OsuUserProfile(

    val id: String,
    val name: String,
    val joinDate: String,
    val country: String,

    val count300: Long,
    val count100: Long,
    val count50: Long,

    val playCount: Int,
    val rankedScore: Long,
    val totalScore: Long,

    val performanceRank: Int,
    val localRank: Int,
    val level: Double,
    val performancePoints: Double,
    val accuracy: Double,

    val rankSS: Int,
    val rankSSPlus: Int,
    val rankS: Int,
    val rankSPlus: Int,
    val rankA: Int

)