package net.jeremystevens.apipractice.dagger

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.features.FeatureProvider
import net.jeremystevens.apipractice.features.coroutine.CoroutineFeatureProvider
import net.jeremystevens.apipractice.features.coroutine.dagger.CoroutineModule
import net.jeremystevens.apipractice.features.coroutine.dagger.CoroutineScope
import net.jeremystevens.apipractice.features.coroutine.ui.CoroutineFragment
import javax.inject.Scope

@Scope
annotation class MainActivityScope

@Module
interface MainActivityModule {

    @ContributesAndroidInjector(modules = [(CoroutineModule::class)])
    @CoroutineScope
    fun coroutineInjector(): CoroutineFragment

    @Binds
    @MainActivityScope
    fun context(mainActivity: MainActivity): Context

    @Binds
    @MainActivityScope
    fun featureProvider(coroutineFeatureProvider: CoroutineFeatureProvider): FeatureProvider
}
