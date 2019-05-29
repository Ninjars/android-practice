package net.jeremystevens.apipractice.features.swapi.people.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_coroutine.*
import net.jeremystevens.apipractice.R
import net.jeremystevens.apipractice.features.icongenerator.IconRepository
import javax.inject.Inject

class PeopleFragment : Fragment(), PeopleContract.View {

    @Inject
    lateinit var presenter: PeopleContract.Presenter

    @Inject
    lateinit var iconRepository: IconRepository

    private lateinit var adapter: PeopleAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_coroutine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PeopleAdapter(iconRepository)
        contentView.adapter = adapter

        presenter.attach(this)

        addButton.setOnClickListener { presenter.addNewEntry() }
        addButton.setOnLongClickListener { presenter.addEntryBatch() }
        sortButton.setOnClickListener { presenter.toggleSortMode() }
    }

    override fun onDestroyView() {
        presenter.detach()
        super.onDestroyView()
    }

    override fun display(model: PeopleContract.ViewModel) {
        when (model) {
            is PeopleContract.ViewModel.Loading -> {
                sortButton.visibility = View.INVISIBLE
                addButton.visibility = View.INVISIBLE
                progressSpinner.visibility = View.VISIBLE
            }
            is PeopleContract.ViewModel.DataModel -> {
                sortButton.visibility = View.VISIBLE
                addButton.visibility = View.VISIBLE
                progressSpinner.visibility = View.INVISIBLE
                adapter.setDataset(model.people)

                val sortDrawableId = when (model.sortMode) {
                    PeopleContract.SortMode.ID -> R.drawable.ic_sort_by_alpha_white_24dp
                    PeopleContract.SortMode.ALPHABETICAL -> R.drawable.ic_sort_white_24dp
                }
                sortButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), sortDrawableId))
            }
        }
    }

    override fun showError(model: PeopleContract.ErrorModel) {
        return when (model) {
            is PeopleContract.ErrorModel.NetworkError ->
                Toast.makeText(context, "Network error: ${model.errorCode}", Toast.LENGTH_SHORT).show()

            PeopleContract.ErrorModel.NoMoreAvailable ->
                Toast.makeText(context, "No more entries available", Toast.LENGTH_SHORT).show()

            PeopleContract.ErrorModel.FailedToFetch ->
                Toast.makeText(context, "Failed to fetch new entry", Toast.LENGTH_SHORT).show()

            PeopleContract.ErrorModel.NoNetwork ->
                Toast.makeText(context, "Unable to reach server", Toast.LENGTH_SHORT).show()

            PeopleContract.ErrorModel.Unknown ->
                Toast.makeText(context, "Unexpected problem encountered!", Toast.LENGTH_SHORT).show()

        }
    }
}
