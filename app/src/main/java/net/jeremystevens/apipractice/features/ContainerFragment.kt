package net.jeremystevens.apipractice.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import net.jeremystevens.apipractice.R

class ContainerFragment(private val contents: Fragment) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_container, container, false)
        replaceFragment(contents, false)
        return view
    }

    fun replaceFragment(fragment: Fragment, addToBackstack: Boolean) {
        if (addToBackstack) {
            childFragmentManager.beginTransaction()
                .replace(R.id.hosted_fragment, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            childFragmentManager.beginTransaction()
                .replace(R.id.hosted_fragment, fragment)
                .commit()
        }
    }

    companion object {
        fun newInstance(fragment: Fragment) : ContainerFragment {
            return ContainerFragment(fragment)
        }
    }

    private fun handleBackPressed(fragmentManager: FragmentManager) : Boolean {
        for (frag in fragmentManager.fragments) {
            if (frag.isVisible && frag is ContainerFragment) {
                return frag.onBackPressed()
            }
        }
        return false
    }

    fun onBackPressed(): Boolean {
        return if (handleBackPressed(childFragmentManager)) {
            true
        } else if (userVisibleHint && childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            true
        } else {
            false
        }
    }
}