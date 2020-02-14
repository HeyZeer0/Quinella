package net.heyzeer0.quinella.core.enums

import net.heyzeer0.quinella.getDataFolder
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

enum class ImageTemplate(
    private val imageFile: String
) {

    OSU_PLAYER_PROFILE("osu-player-profile.png"),
    OSU_MATCH_PROFILE("osu-match-profile.png");

    private val imageFolder = File(getDataFolder(), "images")
    val image: BufferedImage = ImageIO.read(File(imageFolder, imageFile))

}