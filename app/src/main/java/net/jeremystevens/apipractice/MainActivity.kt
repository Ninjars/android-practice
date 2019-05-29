package net.jeremystevens.apipractice

import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.jeremystevens.apipractice.features.swapi.people.ui.PeopleFragment

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var pagerAdapter: BottomBarAdapter

    companion object {
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupBottomNavigation()

        navigation.currentItem = savedInstanceState?.getInt(SAVED_STATE_CURRENT_TAB_KEY) ?: 0
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SAVED_STATE_CURRENT_TAB_KEY, navigation.currentItem)
        super.onSaveInstanceState(outState)
    }

    private fun setupBottomNavigation() {
        navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        val navItems = arrayListOf(
            AHBottomNavigationItem(getString(R.string.nav_tab_1), R.drawable.ic_whatshot_24dp),
            AHBottomNavigationItem(getString(R.string.nav_tab_2), R.drawable.ic_dashboard_black_24dp)
        )
        navigation.addItems(navItems)

        pagerAdapter = BottomBarAdapter(supportFragmentManager)
        pagerAdapter.addFragment(PeopleFragment())
        pagerAdapter.addFragment(PeopleFragment()) // TODO: different second fragment

        container.setPagingEnabled(false)
        container.adapter = pagerAdapter

        navigation.setOnTabSelectedListener { position, wasSelected ->
            if (wasSelected) {
                false
            } else {
                container.currentItem = position
                true
            }
        }
    }
}
