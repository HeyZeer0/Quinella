package net.heyzeer0.quinella.features.enums

import net.heyzeer0.quinella.core.enums.ImageTemplate

enum class OsuModifier(

    val id: Int,
    val shortName: String,
    val image: ImageTemplate?

) {

    NO_FAIL(1, "NF", ImageTemplate.OSU_MOD_NO_FAIL),
    EASY(2, "EZ", ImageTemplate.OSU_MOD_EASY),
    HIDDEN(8, "HD", ImageTemplate.OSU_MOD_HIDDEN),
    HARD_ROCK(16, "HR", ImageTemplate.OSU_MOD_HARD_ROCK),
    SUDDEN_DEATH(32, "SD", ImageTemplate.OSU_MOD_SUDDEN_DEATH),
    DOUBLE_TIME(64, "DT", ImageTemplate.OSU_MOD_DOUBLE_TIME),
    RELAX(128, "RX", null),
    HALF_TIME(256, "HT", null),
    NIGHT_CORE(512, "NC", ImageTemplate.OSU_MOD_NIGHT_CORE),
    FLASHLIGHT(1024, "FL", ImageTemplate.OSU_MOD_FLASHLIGHT),
    AUTOPLAY(2048, "AP", null),
    SPUN_OUT(4096, "SO", null),
    RELAX2(8192, "RX2", null),
    PERFECT(16384, "PF", ImageTemplate.OSU_MOD_PERFECT);

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