package net.jeremystevens.apipractice.features.icongenerator

import android.graphics.Bitmap
import android.graphics.Color
import android.util.SparseArray
import androidx.core.util.contains
import javax.inject.Inject
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

    private fun generateIconColours(random: Random): IntArray {
        val color1 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        val color2 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

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