package net.jeremystevens.apipractice.features.starwars.dagger

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.jeremystevens.apipractice.features.starwars.network.SWAPIService
import net.jeremystevens.apipractice.features.starwars.ui.SWContract
import net.jeremystevens.apipractice.features.starwars.ui.SWPresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Scope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@Scope
annotation class StarWarsScope

@Module(includes = [StarWarsModule.Internal::class])
class StarWarsModule {

    @Provides
    @StarWarsScope
    fun retrofitService(): SWAPIService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


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
        fun presenter(SWPresenter: SWPresenterImpl): SWContract.Presenter
    }
}