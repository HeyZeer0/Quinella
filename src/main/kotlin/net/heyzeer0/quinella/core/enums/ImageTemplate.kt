package net.heyzeer0.quinella.core.enums

import net.heyzeer0.quinella.getDataFolder
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

enum class ImageTemplate(
    private val imageFile: String
) {

    OSU_PLAYER_PROFILE("osu-player-profile.png"),
    OSU_MATCH_PROFILE("osu-match-profile.png"),
    OSU_MOD_NO_FAIL("osu-mod-no-fail.png"),
    OSU_MOD_EASY("osu-mod-easy.png"),
    OSU_MOD_HIDDEN("osu-mod-hidden.png"),
    OSU_MOD_HARD_ROCK("osu-mod-hard-rock.png"),
    OSU_MOD_SUDDEN_DEATH("osu-mod-sudden-death.png"),
    OSU_MOD_DOUBLE_TIME("osu-mod-double-time.png"),
    OSU_MOD_NIGHT_CORE("osu-mod-night-core.png"),
    OSU_MOD_FLASHLIGHT("osu-mod-flashlight.png"),
    OSU_MOD_PERFECT("osu-mod-perfect.png");

    private val imageFolder = File(getDataFolder(), "images")
    val image: BufferedImage = ImageIO.read(File(imageFolder, imageFile))

}