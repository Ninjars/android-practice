package net.jeremystevens.apipractice.features.swapi.planet.domain

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.set
import net.jeremystevens.apipractice.features.swapi.network.SWAPIService
import javax.inject.Inject

class PlanetRepository @Inject constructor(private val service: SWAPIService) {

    private val planets = SparseArray<PlanetData>()

    suspend fun getPlanet(id: Int): PlanetData {
        if (planets.contains(id)) return planets.get(id)
        val planet = service.getPlanet(id).await()
        val data = PlanetData(id, planet.name, planet.population, planet.climate, planet.terrain, planet.diameter, planet.orbital_period, planet.rotation_period)
        planets[id] = data
        return data
    }
}

data class PlanetData(
    val id: Int,
    val name: String,
    val population: String,
    val climate: String,
    val terrain: String,
    val diameter: String,
    val orbital_period: String,
    val rotation_period: String
)
