package net.jeremystevens.apipractice.dagger

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.features.starwars.dagger.StarWarsModule
import net.jeremystevens.apipractice.features.starwars.dagger.StarWarsScope
import net.jeremystevens.apipractice.features.starwars.ui.SWFragment
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
        fun coroutineInjector(): SWFragment

        @Binds
        @MainActivityScope
        fun context(mainActivity: MainActivity): Context
    }
}
