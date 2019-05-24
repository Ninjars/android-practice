package net.jeremystevens.apipractice.features.starwars.ui

import kotlinx.coroutines.*
import net.jeremystevens.apipractice.features.starwars.domain.PersonRepository
import net.jeremystevens.apipractice.features.starwars.domain.PersonData
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class SWPresenterImpl @Inject constructor(private val repository: PersonRepository) : SWContract.Presenter {

    private var view: SWContract.View? = null

    private var runningJob: Job? = null
    private var currentPersonIndex = 0
    private val people = ArrayList<PersonData>()

    private var sortMode = SWContract.SortMode.ID

    override fun attach(view: SWContract.View) {
        Timber.i("attach")
        this.view = view
        view.display(SWContract.ViewModel.DataModel(people, sortMode))
    }

    override fun detach() {
        Timber.i("detach")
        runningJob?.apply { cancel() }
        view = null
    }

    override fun toggleSortMode() {
        view?.display(SWContract.ViewModel.Loading)
        sortMode = when (sortMode) {
            SWContract.SortMode.ID -> SWContract.SortMode.ALPHABETICAL
            SWContract.SortMode.ALPHABETICAL -> SWContract.SortMode.ID
        }
        GlobalScope.launch {
            displayViewData(this)
        }
    }

    override fun addNewEntry() {
        currentPersonIndex.also {
            view?.display(SWContract.ViewModel.Loading)

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
        view?.display(SWContract.ViewModel.Loading)
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
            is HttpException -> SWContract.ErrorModel.NetworkError(exception.code())
            is ArrayIndexOutOfBoundsException -> SWContract.ErrorModel.NoMoreAvailable
            is NoSuchElementException -> SWContract.ErrorModel.FailedToFetch
            is UnknownHostException -> SWContract.ErrorModel.NoNetwork
            else -> {
                Timber.e(exception, "unexpected exception getting person")
                SWContract.ErrorModel.Unknown
            }
        }
        launchViewError(scope, errorModel)
    }

    private fun launchViewError(scope: CoroutineScope, error: SWContract.ErrorModel) {
        scope.launch(Dispatchers.Main) {
            view?.showError(error)
        }
    }

    private fun displayViewData(scope: CoroutineScope) {
        when (sortMode) {
            SWContract.SortMode.ID -> people.sortBy { it.id }
            SWContract.SortMode.ALPHABETICAL -> people.sortBy { it.name }
        }
        scope.launch(Dispatchers.Main) {
            view?.display(SWContract.ViewModel.DataModel(people, sortMode))
        }
    }
}
