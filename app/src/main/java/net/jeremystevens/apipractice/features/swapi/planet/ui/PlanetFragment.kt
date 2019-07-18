package net.jeremystevens.apipractice.features.swapi.planet.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PlanetFragment : Fragment(), PlanetContract.View {

    @Inject
    lateinit var presenter: PlanetContract.Presenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun display(model: PlanetContract.ViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(model: PlanetContract.ErrorModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val KEY_ID: String = "PLANET_ID"

        fun create(id: Int) : PlanetFragment {
            val bundle = Bundle().apply {
                putInt(KEY_ID, id)
            }
            return PlanetFragment().apply { arguments = bundle }
        }
    }
}