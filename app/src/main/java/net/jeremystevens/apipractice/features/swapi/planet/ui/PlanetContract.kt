package net.jeremystevens.apipractice.features.swapi.planet.ui

import net.jeremystevens.apipractice.BasePresenter

object PlanetContract {
    interface View {
        fun display(model: ViewModel)
        fun showError(model: ErrorModel)
    }

    interface Presenter : BasePresenter<View> {
        fun setPlanetId(id: Int)
    }

    sealed class ViewModel {
        object Loading: ViewModel()
        data class DataModel(val name: String) : ViewModel()
    }

    sealed class ErrorModel {
        object NoNetwork : ErrorModel()
        object Unknown : ErrorModel()

        data class NetworkError(val errorCode: Int) : ErrorModel()
    }
}