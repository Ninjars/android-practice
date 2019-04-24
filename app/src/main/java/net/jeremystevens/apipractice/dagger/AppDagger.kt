package net.jeremystevens.apipractice.dagger

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import net.jeremystevens.apipractice.MainActivity
import net.jeremystevens.apipractice.MyApplication
import net.jeremystevens.apipractice.features.coroutine.dagger.MainActivityModule
import net.jeremystevens.apipractice.features.coroutine.dagger.MainActivityScope
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivitiesModule::class
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
class AppModule {}

@Module
interface ActivitiesModule {
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    @MainActivityScope
    fun contributeActivityAndroidInjector(): MainActivity
}
