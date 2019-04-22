package net.jeremystevens.apipractice.dagger

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: Application) {
    @Provides
    fun application(): Application {
        return app
    }
}
