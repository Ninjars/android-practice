package net.jeremystevens.apipractice.features.swapi.people.domain

import android.graphics.Bitmap
import android.util.SparseArray
import androidx.core.util.contains
import net.jeremystevens.apipractice.features.graphics.IconGenerator
import javax.inject.Inject
import kotlin.random.Random

class IconRepository @Inject constructor(private val iconGenerator: IconGenerator) {

    private val icons = SparseArray<Bitmap>()

    fun getIcon(id: Int): Bitmap {
        if (icons.contains(id)) return icons.get(id)

        val random = Random(id)
        val icon = iconGenerator.createIcon(random)
        icons.put(id, icon)
        return icon
    }
}
