package net.heyzeer0.quinella.core.utilities

import net.heyzeer0.quinella.httpClient
import okhttp3.Request
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

fun getImageFromUrl(url: String):BufferedImage? {
    val request = Request.Builder().url(url).build()
    val response = httpClient.newCall(request).execute()

    if (!response.isSuccessful) return null

    return ImageIO.read(response.body()!!.byteStream())
}

fun Graphics2D.drawStringWithWidthLimit(text: String, x: Int, y: Int, maxWidth: Int) {
    val initSize = font.size

    val initialHeight = fontMetrics.height
    while (fontMetrics.stringWidth(text) > maxWidth) {
        font = font.deriveFont(font.size -1f)
    }

    drawString(text, x, y - ((initialHeight - fontMetrics.height)/2))
    font = font.deriveFont(initSize)
}

fun Graphics2D.drawCenteredString(text: String, rect: Rectangle, fontSize: Float = 0f) {
    if (fontSize > 0) font = font.deriveFont(fontSize)

    val x = rect.x + (rect.width - fontMetrics.stringWidth(text)) / 2
    val y = rect.y + ((rect.height - fontMetrics.height) / 2) + fontMetrics.ascent

    drawString(text, x, y)
}

fun BufferedImage.resize(width: Int, height: Int): BufferedImage {
    val tmp = getScaledInstance(width, height, Image.SCALE_SMOOTH)
    val newImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    val graphics = newImage.createGraphics()
    graphics.drawImage(tmp, 0, 0, null)
    graphics.dispose()

    return newImage
}

fun BufferedImage.scale(width: Int, height: Int): BufferedImage {
    val newImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    val originalWidth = getWidth()
    val originalHeight = getHeight()

    for (x in 0..width) {
        for (y in 0..height) {
            val color = getRGB(x * originalWidth / width, y * originalHeight / height)
            newImage.setRGB(x, y, color)
        }
    }

    return newImage
}

fun BufferedImage.copy(): BufferedImage {
    val newImage = BufferedImage(width, height, type)

    val graphics = newImage.createGraphics()
    graphics.drawImage(this, 0, 0, null)
    graphics.dispose()

    return newImage
}