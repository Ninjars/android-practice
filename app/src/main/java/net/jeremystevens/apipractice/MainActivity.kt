package net.jeremystevens.apipractice

import android.os.Bundle
import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.jeremystevens.apipractice.features.FeatureProvider
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private var savedStateSparseArray = SparseArray<Fragment.SavedState>()
    private var currentSelectItemId = R.id.navigation_home

    @Inject
    lateinit var featureProvider: FeatureProvider

    companion object {
        // TODO: put these values into the activity save instance state to allow
        // TODO: activity restoration to also restore fragment states
        const val SAVED_STATE_CONTAINER_KEY = "ContainerKey"
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
//                message.setText(R.string.title_home)
                swapFragments(R.id.navigation_home, "string")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                swapFragments(item.itemId, "secondTab")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun swapFragments(@IdRes actionId: Int, key: String) {
        if (supportFragmentManager.findFragmentByTag(key) == null) {
            savedFragmentState(actionId)
            createFragment(actionId, key)
        }
    }

    private fun createFragment(actionId: Int, key: String) {
        // TODO: load fragment depending on actionId
        val fragment = featureProvider.getFragment()
        fragment.setInitialSavedState(savedStateSparseArray[actionId])
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, key)
            .commit()
    }

    private fun savedFragmentState(actionId: Int) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment != null) {
            savedStateSparseArray.put(
                currentSelectItemId,
                supportFragmentManager.saveFragmentInstanceState(currentFragment)
            )
        }
        currentSelectItemId = actionId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            savedStateSparseArray = it.getSparseParcelableArray(SAVED_STATE_CONTAINER_KEY) ?: SparseArray()
            currentSelectItemId = it.getInt(SAVED_STATE_CURRENT_TAB_KEY)
        }

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            swapFragments(R.id.navigation_home, "string")
        }
    }
}
