package net.jeremystevens.apipractice.features.graphics

import android.graphics.Color
import kotlin.math.roundToInt

object GraphicsUtils {
    fun generateColor(hue: Double, sat: Double, value: Double): Int {
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
