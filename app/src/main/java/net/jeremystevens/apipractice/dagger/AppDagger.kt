package net.jeremystevens.apipractice.dagger

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.MyApplication
import net.jeremystevens.apipractice.features.swapi.dagger.StarWarsServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivitiesModule::class,
        StarWarsServices::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }
}

@Module
class AppModule {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}

@Module
interface ActivitiesModule {
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    @MainActivityScope
    fun mainActivityInjector(): MainActivity
}
