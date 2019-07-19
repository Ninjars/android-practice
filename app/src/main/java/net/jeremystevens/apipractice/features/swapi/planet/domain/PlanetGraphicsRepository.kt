package net.jeremystevens.apipractice.features.swapi.planet.domain

import android.graphics.Bitmap
import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set
import net.jeremystevens.apipractice.features.graphics.PlanetGenerator
import net.jeremystevens.apipractice.features.swapi.planet.ui.PlanetContract
import javax.inject.Inject

class PlanetGraphicsRepository @Inject constructor(
    private val planetGenerator: PlanetGenerator
) {

    private val defaultBitmap: Bitmap = planetGenerator.createDefaultGraphic()

    private val planetBitmaps = SparseArray<Bitmap>()

    fun getPlanetGraphic(planet: PlanetContract.ViewModel.DataModel): Bitmap {
        val id = planet.hashCode()
        if (planetBitmaps.contains(id)) return planetBitmaps.get(id)
        val bitmap = planetGenerator.createPlanetBitmap(planet)
        planetBitmaps[id] = bitmap
        return bitmap
    }

    fun getDefaultGraphic(): Bitmap {
        return defaultBitmap
    }
}
