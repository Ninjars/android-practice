package net.jeremystevens.apipractice.features.icongenerator

import android.graphics.Bitmap
import android.graphics.Color
import android.util.SparseArray
import androidx.core.util.contains
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

class IconRepository @Inject constructor() {

    companion object {
        private const val ICON_WIDTH = 8
        private const val ICON_HEIGHT = 8
        private const val BITMAP_SCALE = 8
    }

    private val icons = SparseArray<Bitmap>()

    fun getIcon(id: Int): Bitmap {
        if (icons.contains(id)) return icons.get(id)

        val random = Random(id)
        val icon = createIcon(random)
        val scaledIcon = Bitmap.createScaledBitmap(icon, icon.width * BITMAP_SCALE, icon.height * BITMAP_SCALE, false)
        icons.put(id, scaledIcon)
        icon.recycle()
        return scaledIcon
    }

    private fun createIcon(random: Random): Bitmap {
        val colors = generateIconColours(random)
        return Bitmap.createBitmap(colors, 0, ICON_WIDTH, ICON_WIDTH, ICON_HEIGHT, Bitmap.Config.ARGB_8888)
    }

    // credit to https://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/ for algorithm
    private fun generateIconColours(random: Random): IntArray {
        val primaryHue = random.nextDouble()
        // offset second hue to ensure colours aren't too similar
        val secondaryHue = (primaryHue + 0.3 + (random.nextDouble() * 0.4)) % 1
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

    private fun generateColor(hue: Double, sat: Double, value: Double) : Int {
        val goldenRationConjugate = 0.618033988749895
        val hueMod = (hue + goldenRationConjugate) % 1

        val hueInt = Math.floor(hueMod * 6).toInt()
        val factor = hueMod * 6 - hueInt

        val p = value * (1 - sat)
        val q = value * (1 - factor * sat)
        val t = value * (1 - (1 - factor) * sat)

        val rgb = when (hueInt) {
            1 -> arrayOf(q, value, p)
            2 -> arrayOf(p, value, t)
            3 -> arrayOf(p, q, value)
            4 -> arrayOf(t, p, value)
            5 -> arrayOf(value, p, q)
            else -> arrayOf(value, t, p)
        }
        return Color.argb(255, (rgb[0] * 255).roundToInt(), (rgb[1] * 255).roundToInt(), (rgb[2] * 255).roundToInt())
    }
}