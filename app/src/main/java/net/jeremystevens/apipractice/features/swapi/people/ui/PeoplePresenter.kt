package net.jeremystevens.apipractice.features.swapi.people.ui

import kotlinx.coroutines.*
import net.jeremystevens.apipractice.features.swapi.people.domain.PersonRepository
import net.jeremystevens.apipractice.features.swapi.people.domain.PersonData
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class PeoplePresenter @Inject constructor(private val repository: PersonRepository) : PeopleContract.Presenter {

    private var view: PeopleContract.View? = null

    private var runningJob: Job? = null
    private var currentPersonIndex = 0
    private val people = ArrayList<PersonData>()

    private var sortMode = PeopleContract.SortMode.ID

    override fun attach(view: PeopleContract.View) {
        Timber.i("attach")
        this.view = view
        view.display(PeopleContract.ViewModel.DataModel(people, sortMode))
    }

    override fun detach() {
        Timber.i("detach")
        runningJob?.apply { cancel() }
        view = null
    }

    override fun toggleSortMode() {
        view?.display(PeopleContract.ViewModel.Loading)
        sortMode = when (sortMode) {
            PeopleContract.SortMode.ID -> PeopleContract.SortMode.ALPHABETICAL
            PeopleContract.SortMode.ALPHABETICAL -> PeopleContract.SortMode.ID
        }
        GlobalScope.launch {
            displayViewData(this)
        }
    }

    override fun addNewEntry() {
        currentPersonIndex.also {
            view?.display(PeopleContract.ViewModel.Loading)

            GlobalScope.launch {
                try {
                    people.add(repository.getPerson(it))

                } catch (e: Exception) {
                    handleException(this, e)

                } finally {
                    displayViewData(this)
                }
            }
        }
        currentPersonIndex++
    }

    override fun addEntryBatch(): Boolean {
        view?.display(PeopleContract.ViewModel.Loading)
        GlobalScope.launch {
            try {
                val newPeople = repository.getPeopleBatch(currentPersonIndex)
                currentPersonIndex = newPeople.size
                people.clear()
                people.addAll(newPeople)

            } catch (e: Exception) {
                handleException(this, e)

            } finally {
                displayViewData(this)
            }
        }
        return true
    }

    private fun handleException(scope: CoroutineScope, exception: java.lang.Exception) {
        val errorModel = when (exception) {
            is HttpException -> PeopleContract.ErrorModel.NetworkError(exception.code())
            is ArrayIndexOutOfBoundsException -> PeopleContract.ErrorModel.NoMoreAvailable
            is NoSuchElementException -> PeopleContract.ErrorModel.FailedToFetch
            is UnknownHostException -> PeopleContract.ErrorModel.NoNetwork
            else -> {
                Timber.e(exception, "unexpected exception getting person")
                PeopleContract.ErrorModel.Unknown
            }
        }
        launchViewError(scope, errorModel)
    }

    private fun launchViewError(scope: CoroutineScope, error: PeopleContract.ErrorModel) {
        scope.launch(Dispatchers.Main) {
            view?.showError(error)
        }
    }

    private fun displayViewData(scope: CoroutineScope) {
        when (sortMode) {
            PeopleContract.SortMode.ID -> people.sortBy { it.id }
            PeopleContract.SortMode.ALPHABETICAL -> people.sortBy { it.name }
        }
        scope.launch(Dispatchers.Main) {
            view?.display(PeopleContract.ViewModel.DataModel(people, sortMode))
        }
    }
}
