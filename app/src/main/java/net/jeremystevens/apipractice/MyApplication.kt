package net.jeremystevens.apipractice

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import net.jeremystevens.apipractice.dagger.DaggerAppComponent

class MyApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
