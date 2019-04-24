package net.jeremystevens.apipractice

interface BasePresenter<T> {
    fun attach(view: T)

    fun detach()
}