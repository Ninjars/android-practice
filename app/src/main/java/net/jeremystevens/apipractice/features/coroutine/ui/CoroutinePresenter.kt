package net.jeremystevens.apipractice.features.coroutine.ui

import kotlinx.coroutines.*
import net.jeremystevens.apipractice.features.coroutine.domain.PersonRepository
import net.jeremystevens.apipractice.features.coroutine.domain.PersonData
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class CoroutinePresenterImpl @Inject constructor(private val repository: PersonRepository) : Coroutine.Presenter {

    private var view: Coroutine.View? = null

    private var runningJob: Job? = null
    private var currentPersonIndex = 0
    private val people = ArrayList<PersonData>()

    override fun attach(view: Coroutine.View) {
        Timber.i("attach")
        this.view = view
        view.display(Coroutine.ViewModel.DataModel(people))
    }

    override fun detach() {
        Timber.i("detach")
        runningJob?.apply { cancel() }
        view = null
    }

    override fun addNewEntry() {
        currentPersonIndex.also {
            view?.display(Coroutine.ViewModel.Loading)

            GlobalScope.launch {
                try {
                    people.add(repository.getPerson(it))

                } catch (networkException: HttpException) {
                    launchViewError(this, Coroutine.ErrorModel.NetworkError(networkException.code()))

                } catch (noMoreException: ArrayIndexOutOfBoundsException) {
                    launchViewError(this, Coroutine.ErrorModel.NoMoreAvailable)

                } catch (notFoundException: NoSuchElementException) {
                    launchViewError(this, Coroutine.ErrorModel.FailedToFetch)
                }

                launch(Dispatchers.Main) {
                    view?.display(Coroutine.ViewModel.DataModel(people))
                }
            }
        }
        currentPersonIndex++
    }

    private fun launchViewError(scope: CoroutineScope, error: Coroutine.ErrorModel) {
        scope.launch(Dispatchers.Main) {
            view?.showError(error)
        }
    }
}
