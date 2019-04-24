package net.jeremystevens.apipractice.features.coroutine

import android.content.Context
import androidx.fragment.app.Fragment
import net.jeremystevens.apipractice.R
import net.jeremystevens.apipractice.features.FeatureProvider
import net.jeremystevens.apipractice.features.coroutine.ui.CoroutineFragment
import javax.inject.Inject

class CoroutineFeatureProvider @Inject constructor(private val context: Context) : FeatureProvider {
    override fun getTabLabel(): String = context.getString(R.string.tab_coroutine)

    override fun getFragment(): Fragment = CoroutineFragment()
}
