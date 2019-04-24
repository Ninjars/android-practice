package net.jeremystevens.apipractice.features.coroutine.dagger

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.features.FeatureProvider
import net.jeremystevens.apipractice.features.coroutine.CoroutineFeatureProvider
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class MainActivityScope

@Module
interface MainActivityModule {
    @Binds
    @MainActivityScope
    fun context(mainActivity: MainActivity): Context

    @Binds
    @MainActivityScope
    fun featureProvider(coroutineFeatureProvider: CoroutineFeatureProvider): FeatureProvider
}
