package net.jeremystevens.apipractice.features.starwars.ui

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
import javax.inject.Inject

class SWFragment : Fragment(), SWContract.View {

    @Inject
    lateinit var presenter: SWContract.Presenter

    private lateinit var adapter: PersonAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_coroutine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PersonAdapter()
        contentView.adapter = adapter

        presenter.attach(this)

        addButton.setOnClickListener { presenter.addNewEntry() }
        sortButton.setOnClickListener { presenter.toggleSortMode() }
    }

    override fun onDestroyView() {
        presenter.detach()
        super.onDestroyView()
    }

    override fun display(model: SWContract.ViewModel) {
        when (model) {
            is SWContract.ViewModel.Loading -> {
                sortButton.visibility = View.INVISIBLE
                addButton.visibility = View.INVISIBLE
                progressSpinner.visibility = View.VISIBLE
            }
            is SWContract.ViewModel.DataModel -> {
                sortButton.visibility = View.VISIBLE
                addButton.visibility = View.VISIBLE
                progressSpinner.visibility = View.INVISIBLE
                adapter.setDataset(model.people)

                val sortDrawableId = when (model.sortMode) {
                    SWContract.SortMode.ID -> R.drawable.ic_sort_by_alpha_white_24dp
                    SWContract.SortMode.ALPHABETICAL -> R.drawable.ic_sort_white_24dp
                }
                sortButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), sortDrawableId))
            }
        }
    }

    override fun showError(model: SWContract.ErrorModel) {
        when (model) {
            is SWContract.ErrorModel.NoMoreAvailable ->
                Toast.makeText(context, "No more entries available", Toast.LENGTH_SHORT).show()

            is SWContract.ErrorModel.FailedToFetch ->
                Toast.makeText(context, "Failed to fetch new entry", Toast.LENGTH_SHORT).show()

            is SWContract.ErrorModel.NetworkError ->
                Toast.makeText(context, "Network error: ${model.errorCode}", Toast.LENGTH_SHORT).show()
        }
    }
}
