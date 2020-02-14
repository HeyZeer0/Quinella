package net.heyzeer0.quinella.features.instances

class OsuBeatMapProfile(

    val id: String,
    val setId: String,
    val approved: Boolean,

    val artist: String,
    val title: String,

    val totalLength: Int,
    val hitLength: Int,

    val version: String,

    val difficultSize: Double,
    val difficultOverall: Double,
    val difficultApproach: Double,
    val difficultDrain: Double,
    val difficultAim: Double,
    val difficultSpeed: Double,
    val difficultRating: Double,

    val bpm: Int,
    val maxCombo: Int,
    val mode: Int,

    val hitCircles: Int,
    val sliders: Int,
    val spinners: Int,

    val submitDate: String,
    val approvedDate: String,
    val lastUpdate: String,

    val mapper: String,
    val mapperId: String,

    val favouriteAmount: Int,
    val rating: Double,
    val playCount: Int,
    val passCount: Int

)