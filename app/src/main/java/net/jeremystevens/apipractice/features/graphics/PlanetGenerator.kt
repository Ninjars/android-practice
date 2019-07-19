package net.jeremystevens.apipractice.features.graphics

import android.graphics.*
import androidx.core.graphics.set
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
        canvas.drawCircle(CENTER + xOffset, CENTER + yOffset, radius + (distance * 0.5f), paint)

        return bitmap
    }

    fun createPlanetBitmap(planetData: PlanetContract.ViewModel.DataModel): Bitmap {
        val bitmap = Bitmap.createBitmap(BITMAP_SIZE, BITMAP_SIZE, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        val canvas = Canvas(bitmap)

        val random = Random(planetData.hashCode())
        val radius = 40 + random.nextFloat() * 60

        val primaryHue = generatePrimaryHue(random, planetData.climate)
        val primarySat = generateSaturation(random, planetData.climate)
        val primaryVal = generateValue(random, planetData.climate)
        val primeColour = Color.HSVToColor(floatArrayOf(primaryHue * 360, primarySat, primaryVal))

        val paint = Paint().apply {
            color = primeColour
        }
        canvas.drawCircle(CENTER, CENTER, radius, paint)

        applyNoise(random, canvas, radius)

        val distance = radius * 0.2f + random.nextFloat() * radius * 0.5f
        val angle = Math.PI * 0.7 + random.nextDouble() * Math.PI * 0.6
        val brightnessMask = createBrightnessMask(distance, angle, radius)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        canvas.drawBitmap(brightnessMask, 0f, 0f, paint)

        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, bitmap.width * BITMAP_SCALE, bitmap.height * BITMAP_SCALE, false)
        bitmap.recycle()
        brightnessMask.recycle()
        return scaledBitmap
    }

    private fun applyNoise(random: Random, canvas: Canvas, radius: Float) {
        val size = Math.ceil(radius.toDouble()).toInt() * 2
        val noiseArray = Array(size) { FloatArray(size) }

        val noiseBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val paint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY) }

        populateNoise(random, size, 0.02f, noiseArray)
        for (y in 0 until size) {
            for (x in 0 until size) {
                noiseBitmap[x, y] =
                    Color.HSVToColor(floatArrayOf(0f, noiseArray[x][y] * 0.1f, 0.8f + noiseArray[x][y] * 0.2f))
            }
        }
        canvas.drawBitmap(noiseBitmap, CENTER - radius, CENTER - radius, paint)

        populateNoise(random, size, 0.04f, noiseArray)
        for (y in 0 until size) {
            for (x in 0 until size) {
                noiseBitmap[x, y] =
                    Color.HSVToColor(floatArrayOf(0f, noiseArray[x][y] * 0.02f, 0.92f + noiseArray[x][y] * 0.08f))
            }
        }
        canvas.drawBitmap(noiseBitmap, CENTER - radius, CENTER - radius, paint)

        populateNoise(random, size, 0.6f, noiseArray)
        for (y in 0 until size) {
            for (x in 0 until size) {
                noiseBitmap[x, y] =
                    Color.HSVToColor(floatArrayOf(0f, noiseArray[x][y] * 0.01f, 0.95f + noiseArray[x][y] * 0.05f))
            }
        }
        canvas.drawBitmap(noiseBitmap, CENTER - radius, CENTER - radius, paint)
    }

    private fun populateNoise(random: Random, size: Int, frequency: Float, array: Array<FloatArray>) {
        SimplexNoise.generateSimplexNoise(
            random.nextInt(256),
            random.nextInt(256),
            size,
            size,
            frequency,
            array
        )
    }

    private fun generatePrimaryHue(random: Random, climate: String): Float {
        val (offset, variance) = when (climate) {
            "temperate" -> Pair(0.33, 0.15)
            "arid" -> Pair(0.1, 0.06)
            "polluted" -> Pair(0.8, 0.15)
            else -> Pair(random.nextDouble(), 0.0)
        }
        return valueWithVariance(random, offset, variance) % 1
    }

    private fun generateSaturation(random: Random, climate: String): Float {
        return when (climate) {
            "polluted" -> valueWithVariance(random, 0.6, 0.1)
            else -> valueWithVariance(random, 0.9, 0.05)
        }
    }

    private fun generateValue(random: Random, climate: String): Float {
        return when (climate) {
            "temperate" -> valueWithVariance(random, 0.6, 0.1)
            "arid" -> valueWithVariance(random, 0.8, 0.1)
            "polluted" -> valueWithVariance(random, 0.4, 0.1)
            else -> valueWithVariance(random, 0.5, 0.2)
        }
    }

    private fun valueWithVariance(random: Random, value: Double, variance: Double): Float {
        return ((value - variance) + variance * 2 * random.nextDouble()).toFloat()
    }
}
