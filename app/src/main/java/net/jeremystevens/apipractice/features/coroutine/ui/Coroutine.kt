package net.jeremystevens.apipractice.features.coroutine.ui

import net.jeremystevens.apipractice.BasePresenter

interface Coroutine {
    interface View
    interface Presenter : BasePresenter<View>
}