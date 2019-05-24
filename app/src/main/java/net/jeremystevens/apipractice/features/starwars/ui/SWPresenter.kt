package net.jeremystevens.apipractice.features.starwars.ui

import kotlinx.coroutines.*
import net.jeremystevens.apipractice.features.starwars.domain.PersonRepository
import net.jeremystevens.apipractice.features.starwars.domain.PersonData
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class SWPresenterImpl @Inject constructor(private val repository: PersonRepository) : SWContract.Presenter {

    private var view: SWContract.View? = null

    private var runningJob: Job? = null
    private var currentPersonIndex = 0
    private val people = ArrayList<PersonData>()

    override fun attach(view: SWContract.View) {
        Timber.i("attach")
        this.view = view
        view.display(SWContract.ViewModel.DataModel(people))
    }

    override fun detach() {
        Timber.i("detach")
        runningJob?.apply { cancel() }
        view = null
    }

    override fun addNewEntry() {
        currentPersonIndex.also {
            view?.display(SWContract.ViewModel.Loading)

            GlobalScope.launch {
                try {
                    people.add(repository.getPerson(it))

                } catch (networkException: HttpException) {
                    launchViewError(this, SWContract.ErrorModel.NetworkError(networkException.code()))

                } catch (noMoreException: ArrayIndexOutOfBoundsException) {
                    launchViewError(this, SWContract.ErrorModel.NoMoreAvailable)

                } catch (notFoundException: NoSuchElementException) {
                    launchViewError(this, SWContract.ErrorModel.FailedToFetch)
                }

                launch(Dispatchers.Main) {
                    view?.display(SWContract.ViewModel.DataModel(people))
                }
            }
        }
        currentPersonIndex++
    }

    private fun launchViewError(scope: CoroutineScope, error: SWContract.ErrorModel) {
        scope.launch(Dispatchers.Main) {
            view?.showError(error)
        }
    }
}
