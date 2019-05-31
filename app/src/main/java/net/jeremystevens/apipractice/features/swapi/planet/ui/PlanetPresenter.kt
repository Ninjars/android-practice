package net.jeremystevens.apipractice.features.swapi.planet.ui

import kotlinx.coroutines.*
import net.jeremystevens.apipractice.features.swapi.network.PlanetResult
import net.jeremystevens.apipractice.features.swapi.network.SWAPIService
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class PlanetPresenter @Inject constructor(private val service: SWAPIService) : PlanetContract.Presenter {

    private var runningJob: Job? = null
    private var view: PlanetContract.View? = null
    private var planetData: PlanetData? = null

    override fun attach(view: PlanetContract.View) {
        this.view = view
        planetData?.run {
            displayPlanet(this)
        }
    }

    override fun detach() {
        runningJob?.apply { cancel() }
        view = null
        planetData = null
    }

    override fun setPlanetId(id: Int) {
        planetData?.let {
            if (it.id == id) {
                displayPlanet(it)
            } else {
                downloadPlanetInfo(id)
            }
        } ?: downloadPlanetInfo(id)
    }

    private fun downloadPlanetInfo(id: Int) {
        runningJob = GlobalScope.launch {
            try {
                val planetResult = service.getPlanet(id).await()
                planetData = PlanetData(id, planetResult)
            } catch (e: Exception) {
                handleException(this, e)

            } finally {
                planetData?.let {
                    displayPlanet(it)
                }
            }
        }
    }

    private fun displayPlanet(planetData: PlanetData) {
        view?.display(PlanetContract.ViewModel.DataModel(planetData.planetResult.name))
    }

    private fun handleException(scope: CoroutineScope, exception: java.lang.Exception) {
        val errorModel = when (exception) {
            is HttpException -> PlanetContract.ErrorModel.NetworkError(exception.code())
            is UnknownHostException -> PlanetContract.ErrorModel.NoNetwork
            else -> {
                Timber.e(exception, "unexpected exception getting person")
                PlanetContract.ErrorModel.Unknown
            }
        }
        launchViewError(scope, errorModel)
    }

    private fun launchViewError(scope: CoroutineScope, error: PlanetContract.ErrorModel) {
        scope.launch(Dispatchers.Main) {
            view?.showError(error)
        }
    }

    private data class PlanetData(val id: Int, val planetResult: PlanetResult)
}