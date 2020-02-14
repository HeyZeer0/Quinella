package net.heyzeer0.quinella.core.enums

import net.heyzeer0.quinella.getDataFolder
import java.awt.Font
import java.io.File

enum class AvailableFont(
    fontFile: String,
    fontType: Int = Font.TRUETYPE_FONT
) {

    EXO2_BOLD("Exo2-Bold.otf"),
    EXO2_ITALIC("Exo2-Italic.otf"),
    EXO2_REGULAR("Exo2-Regular.otf");

    private val fontFolder = File(getDataFolder(), "fonts")
    val font: Font = Font.createFont(fontType, File(fontFolder, fontFile))

}