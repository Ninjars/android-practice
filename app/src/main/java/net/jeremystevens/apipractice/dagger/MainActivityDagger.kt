package net.jeremystevens.apipractice.dagger

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.features.starwars.dagger.StarWarsModule
import net.jeremystevens.apipractice.features.starwars.dagger.StarWarsScope
import net.jeremystevens.apipractice.features.starwars.ui.SWFragment
import javax.inject.Scope

@Scope
annotation class MainActivityScope

@Module
interface MainActivityModule {

    @ContributesAndroidInjector(modules = [(StarWarsModule::class)])
    @StarWarsScope
    fun coroutineInjector(): SWFragment

    @Binds
    @MainActivityScope
    fun context(mainActivity: MainActivity): Context
}
