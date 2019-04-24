package net.jeremystevens.apipractice.features.coroutine.ui

import timber.log.Timber
import javax.inject.Inject

class CoroutinePresenterImpl @Inject constructor() : Coroutine.Presenter {
    override fun attach(view: Coroutine.View) {
        Timber.i("attach")
    }

    override fun detach() {
        Timber.i("detach")
    }
}
