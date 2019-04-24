package net.jeremystevens.apipractice.features

import androidx.fragment.app.Fragment

interface FeatureProvider {
    fun getTabLabel(): String
    fun getFragment(): Fragment
}
