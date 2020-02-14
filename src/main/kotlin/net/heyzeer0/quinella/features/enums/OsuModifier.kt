package net.heyzeer0.quinella.features.enums

import java.awt.image.BufferedImage

enum class OsuModifier(

    val id: Int,
    val shortName: String,
    val imageFile: String?

) {

    NO_FAIL(1, "NF", "mod-no-fail"),
    EASY(2, "EZ", "mod-easy"),
    HIDDEN(8, "HD", "mod-hidden"),
    HARD_ROCK(16, "HR", "mod-hard-rock"),
    SUDDEN_DEATH(32, "SD", "mod-sudden-death"),
    DOUBLE_TIME(64, "DT", "mod-double-time"),
    RELAX(128, "RX", null),
    HALF_TIME(256, "HT", null),
    NIGHT_CORE(512, "NC", "mod-nightcore"),
    FLASHLIGHT(1024, "FL", "mod-flashlight"),
    AUTOPLAY(2048, "AP", null),
    SPUN_OUT(4096, "SO", null),
    RELAX2(8192, "RX2", null),
    PERFECT(16384, "PF", "mod-perfect");

    lateinit var image: BufferedImage

    fun hasMod(modSum: Int): Boolean {
        return (modSum and id) == id
    }

    companion object {

        fun getMods(modSum: Int): List<OsuModifier> {
            val list = arrayListOf<OsuModifier>()

            for (mod in values()) {
                if (!mod.hasMod(modSum)) continue

                list += mod
            }

            return list
        }

        fun asString(mods: List<OsuModifier>): String {
            if (mods.isEmpty()) return ""

            val builder = StringBuilder()
            for (mod in mods) builder.append(mod.shortName)

            return builder.toString()
        }

    }

}