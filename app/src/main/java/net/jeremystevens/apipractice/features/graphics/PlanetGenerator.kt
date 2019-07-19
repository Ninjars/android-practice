package net.jeremystevens.apipractice.features.graphics

import android.graphics.*
import net.jeremystevens.apipractice.features.graphics.GraphicsUtils.generateColor
import net.jeremystevens.apipractice.features.swapi.planet.ui.PlanetContract
import javax.inject.Inject
import kotlin.random.Random

class PlanetGenerator @Inject constructor() {

    companion object {
        private const val BITMAP_SIZE = 256
        private const val CENTER = 128f
        private const val BITMAP_SCALE = 2

        private const val VALUE_DARKNESS = 0.5f
        private const val VALUE_FULL = 1f
    }

    fun createDefaultGraphic(): Bitmap {
        val distance = 30f
        val angle = Math.PI * 1.33
        val bitmap = createBrightnessMask(distance, angle, 100f)

        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, bitmap.width * BITMAP_SCALE, bitmap.height * BITMAP_SCALE, false)
        bitmap.recycle()
        return scaledBitmap
    }

    private fun createBrightnessMask(distance: Float, angle: Double, radius: Float): Bitmap {
        val bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            color = Color.HSVToColor(floatArrayOf(0f, 0f, VALUE_DARKNESS))
        }
        canvas.drawCircle(CENTER, CENTER, radius, paint)

        val xOffset = distance * (Math.cos(angle)).toFloat()
        val yOffset = distance * (Math.sin(angle)).toFloat()

        paint.color = Color.HSVToColor(floatArrayOf(0f, 0f, VALUE_FULL))
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        canvas.drawCircle(CENTER + xOffset, CENTER + yOffset, radius + (distance / 2), paint)

        return bitmap
    }

    fun createPlanetBitmap(planetData: PlanetContract.ViewModel.DataModel): Bitmap {
        val bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        val canvas = Canvas(bitmap)

        val random = Random(planetData.hashCode())
        val radius = random.nextFloat() * 60 + 40
        val distance = random.nextFloat() * radius * 0.4f + (radius * 0.1f)
        val angle = random.nextFloat() * Math.PI * 0.7 + Math.PI * 0.65
        val brightnessMask = createBrightnessMask(distance, angle, radius)

        val primaryHue = random.nextDouble()
        val primeColour = generateColor(primaryHue, 0.5, 0.9)
        val secondaryHue = (primaryHue + 0.3 + (random.nextDouble() * 0.4)) % 1

        val paint = Paint().apply {
            color = primeColour
        }
        canvas.drawCircle(CENTER, CENTER, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        canvas.drawBitmap(brightnessMask, 0f, 0f, paint)

        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, bitmap.width * BITMAP_SCALE, bitmap.height * BITMAP_SCALE, false)
        bitmap.recycle()
        brightnessMask.recycle()
        return scaledBitmap
    }
}