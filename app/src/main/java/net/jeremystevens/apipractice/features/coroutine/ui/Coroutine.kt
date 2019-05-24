package net.jeremystevens.apipractice.features.coroutine.ui

import net.jeremystevens.apipractice.BasePresenter
import net.jeremystevens.apipractice.features.coroutine.domain.PersonData

interface Coroutine {
    interface View {
        fun display(model: ViewModel)
        fun showError(model: ErrorModel)
    }

    interface Presenter : BasePresenter<View> {
        fun addNewEntry()
    }

    sealed class ViewModel {
        object Loading : ViewModel()

        data class DataModel(val people: List<PersonData>) : ViewModel()
    }

    sealed class ErrorModel {
        object NoMoreAvailable : ErrorModel()
        object FailedToFetch : ErrorModel()

        data class NetworkError(val errorCode: Int) : ErrorModel()
    }
}