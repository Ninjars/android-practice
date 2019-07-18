package net.jeremystevens.apipractice.dagger

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.features.swapi.dagger.StarWarsModule
import net.jeremystevens.apipractice.features.swapi.dagger.StarWarsScope
import net.jeremystevens.apipractice.features.swapi.people.ui.PeopleFragment
import net.jeremystevens.apipractice.features.swapi.planet.ui.PlanetFragment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Scope

@Scope
annotation class MainActivityScope

@Module(includes = [MainActivityModule.Internal::class])
class MainActivityModule {

    @Provides
    @MainActivityScope
    fun okHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Module
    interface Internal {
        @ContributesAndroidInjector(modules = [(StarWarsModule::class)])
        @StarWarsScope
        fun peopleInjector(): PeopleFragment

        @ContributesAndroidInjector(modules = [(StarWarsModule::class)])
        @StarWarsScope
        fun planetInjector(): PlanetFragment

        @Binds
        @MainActivityScope
        fun context(mainActivity: MainActivity): Context
    }
}
