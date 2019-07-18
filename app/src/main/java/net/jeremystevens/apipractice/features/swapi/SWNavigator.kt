package net.jeremystevens.apipractice.features.swapi

import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.features.swapi.planet.ui.PlanetFragment
import javax.inject.Inject

class SWNavigator @Inject constructor(private val mainActivity: MainActivity) {
    fun navigateToPlanet(planetId: Int) {
        mainActivity.openNewFragment(PlanetFragment.create(planetId))
    }
}