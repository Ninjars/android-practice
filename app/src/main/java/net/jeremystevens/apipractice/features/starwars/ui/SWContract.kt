package net.jeremystevens.apipractice.features.starwars.ui

import net.jeremystevens.apipractice.BasePresenter
import net.jeremystevens.apipractice.features.starwars.domain.PersonData

interface SWContract {
    interface View {
        fun display(model: ViewModel)
        fun showError(model: ErrorModel)
    }

    interface Presenter : BasePresenter<View> {
        fun addNewEntry()
        fun addEntryBatch(): Boolean
        fun toggleSortMode()
    }

    sealed class ViewModel {
        object Loading : ViewModel()

        data class DataModel(val people: List<PersonData>, val sortMode: SortMode) : ViewModel()
    }

    sealed class ErrorModel {
        object NoMoreAvailable : ErrorModel()
        object FailedToFetch : ErrorModel()
        object NoNetwork : ErrorModel()
        object Unknown : ErrorModel()

        data class NetworkError(val errorCode: Int) : ErrorModel()
    }

    enum class SortMode { ID, ALPHABETICAL }
}