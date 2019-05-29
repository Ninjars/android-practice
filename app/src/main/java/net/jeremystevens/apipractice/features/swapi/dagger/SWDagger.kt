package net.jeremystevens.apipractice.features.swapi.dagger

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.jeremystevens.apipractice.features.swapi.network.SWAPIService
import net.jeremystevens.apipractice.features.swapi.people.ui.PeopleContract
import net.jeremystevens.apipractice.features.swapi.people.ui.PeoplePresenter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Scope
import okhttp3.OkHttpClient


@Scope
annotation class StarWarsScope

@Module(includes = [StarWarsModule.Internal::class])
class StarWarsModule {

    @Provides
    @StarWarsScope
    fun retrofitService(client: OkHttpClient): SWAPIService {
        return Retrofit.Builder()
            .baseUrl("https://swapi.co/api/")
            .client(client)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SWAPIService::class.java)
    }

    @Module
    interface Internal {
        @Binds
        @StarWarsScope
        fun presenter(peoplePresenter: PeoplePresenter): PeopleContract.Presenter
    }
}