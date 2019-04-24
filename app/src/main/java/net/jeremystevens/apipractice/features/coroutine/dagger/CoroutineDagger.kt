package net.jeremystevens.apipractice.features.coroutine.dagger

import dagger.Binds
import dagger.Module
import net.jeremystevens.apipractice.features.coroutine.ui.Coroutine
import net.jeremystevens.apipractice.features.coroutine.ui.CoroutinePresenterImpl
import javax.inject.Scope

@Scope
annotation class CoroutineScope

@Module
interface CoroutineModule {
    @Binds
    @CoroutineScope
    fun presenter(coroutinePresenter: CoroutinePresenterImpl): Coroutine.Presenter
}