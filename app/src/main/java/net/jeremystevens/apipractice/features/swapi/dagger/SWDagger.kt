package net.jeremystevens.apipractice.features.swapi.dagger

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.jeremystevens.apipractice.features.swapi.network.SWAPIService
import net.jeremystevens.apipractice.features.swapi.people.ui.PeopleContract
import net.jeremystevens.apipractice.features.swapi.people.ui.PeoplePresenter
import net.jeremystevens.apipractice.features.swapi.planet.ui.PlanetContract
import net.jeremystevens.apipractice.features.swapi.planet.ui.PlanetPresenter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Scope
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Scope
annotation class StarWarsScope

@Module(includes = [StarWarsModule.Internal::class])
class StarWarsModule {

    @Module
    interface Internal {
        @Binds
        @StarWarsScope
        fun peoplePresenter(peoplePresenter: PeoplePresenter): PeopleContract.Presenter

        @Binds
        @StarWarsScope
        fun planetPresenter(planetPresenter: PlanetPresenter): PlanetContract.Presenter
    }
}

@Module
class StarWarsServices {

    @Provides
    @Singleton
    fun retrofitService(client: OkHttpClient): SWAPIService {
        return Retrofit.Builder()
            .baseUrl("https://swapi.co/api/")
            .client(client)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SWAPIService::class.java)
    }
}
