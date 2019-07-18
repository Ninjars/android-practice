package net.jeremystevens.apipractice.features.swapi.planet.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_starwars_planet.*
import net.jeremystevens.apipractice.R
import javax.inject.Inject

class PlanetFragment : Fragment(), PlanetContract.View {

    @Inject
    lateinit var presenter: PlanetContract.Presenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_starwars_planet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val planetId = requireArguments().getInt(KEY_ID)

        presenter.attach(this)
        presenter.setPlanetId(planetId)
    }

    override fun onDestroyView() {
        presenter.detach()
        super.onDestroyView()
    }

    override fun display(model: PlanetContract.ViewModel) {
        return when (model) {
            PlanetContract.ViewModel.Loading -> showLoading()
            is PlanetContract.ViewModel.DataModel -> showData(model)
        }
    }

    private fun showData(model: PlanetContract.ViewModel.DataModel) {
        progressSpinner.visibility = View.GONE
        errorView.visibility = View.GONE
        planetContainer.visibility = View.VISIBLE

        planetName.text = model.name
        populationView.text = model.population
        climateView.text = model.climate
        terrainView.text = model.terrain
    }

    private fun showLoading() {
        progressSpinner.visibility = View.VISIBLE
        planetContainer.visibility = View.GONE
        errorView.visibility = View.GONE
    }

    override fun showError(model: PlanetContract.ErrorModel) {
        progressSpinner.visibility = View.GONE
        planetContainer.visibility = View.GONE
        errorView.visibility = View.VISIBLE
        errorView.text = when (model) {
            PlanetContract.ErrorModel.NoNetwork -> "Unable to reach the API"
            PlanetContract.ErrorModel.Unknown -> "Unknown Error"
            is PlanetContract.ErrorModel.NetworkError -> "Network Error ${model.errorCode}"
        }
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