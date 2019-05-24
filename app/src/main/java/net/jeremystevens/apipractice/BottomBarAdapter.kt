package net.jeremystevens.apipractice

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class BottomBarAdapter(fragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(fragmentManager) {

    private val fragments = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }
}