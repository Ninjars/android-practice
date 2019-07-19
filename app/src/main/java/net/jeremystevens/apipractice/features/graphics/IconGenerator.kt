package net.jeremystevens.apipractice.features.graphics

import android.graphics.Bitmap
import net.jeremystevens.apipractice.features.graphics.GraphicsUtils.generateColor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class IconGenerator @Inject constructor() {

    companion object {
        private const val ICON_WIDTH = 8
        private const val ICON_HEIGHT = 8
        private const val BITMAP_SCALE = 8
    }

    fun createIcon(id: Int): Bitmap {
        val random = Random(id)
        val colors = generateIconColours(random)
        val icon = Bitmap.createBitmap(
            colors, 0,
            ICON_WIDTH,
            ICON_WIDTH,
            ICON_HEIGHT, Bitmap.Config.ARGB_8888
        )
        val scaledIcon = Bitmap.createScaledBitmap(icon, icon.width * BITMAP_SCALE, icon.height * BITMAP_SCALE, false)
        icon.recycle()
        return scaledIcon
    }

    // credit to https://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/ for algorithm
    private fun generateIconColours(random: Random): IntArray {
        val primaryHue = random.nextDouble()
        // offset second hue to ensure colours aren't too similar
        val colourOffset = 0.2 + (random.nextDouble() * 0.4)
        val invertFactor = if (random.nextBoolean()) 1 else -1
        val secondaryHue = (primaryHue + (colourOffset * invertFactor)) % 1
        val color1 = generateColor(primaryHue, 0.5, 0.95)
        val color2 = generateColor(secondaryHue, 0.5, 0.95)

        val pixels = IntArray(ICON_WIDTH * ICON_HEIGHT)
        for (y in 0 until ICON_HEIGHT) {
            for (x in 0 until ICON_WIDTH / 2) {
                val color = if (random.nextBoolean()) color1 else color2
                val offset = y * ICON_HEIGHT
                pixels[offset + x] = color
                pixels[offset + ICON_WIDTH - x - 1] = color
            }
        }
        return pixels
    }
}
