package net.jeremystevens.apipractice.features.swapi.planet.ui

import kotlinx.coroutines.*
import net.jeremystevens.apipractice.features.swapi.planet.domain.PlanetRepository
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class PlanetPresenter @Inject constructor(private val repository: PlanetRepository) : PlanetContract.Presenter {

    private var runningJob: Job? = null
    private var view: PlanetContract.View? = null
    private var planetId: Int? = null

    override fun attach(view: PlanetContract.View) {
        this.view = view
        planetId?.run {
            displayPlanet(this)
        }
    }

    override fun detach() {
        runningJob?.apply { cancel() }
        view = null
    }

    override fun setPlanetId(id: Int) {
        planetId = id
        displayPlanet(id)
    }

    private fun displayPlanet(id: Int) {
        view?.display(PlanetContract.ViewModel.Loading)
        runningJob = GlobalScope.launch {
            val planetData = try {
                repository.getPlanet(id)
            } catch (e: Exception) {
                handleException(this, e)
                null
            }
            planetData?.let {
                launch(Dispatchers.Main) {
                    view?.display(
                        PlanetContract.ViewModel.DataModel(
                            planetData.name,
                            planetData.population,
                            planetData.climate,
                            planetData.terrain
                        )
                    )
                }
            }
        }
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
}
