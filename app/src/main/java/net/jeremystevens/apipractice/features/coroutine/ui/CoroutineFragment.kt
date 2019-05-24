package net.jeremystevens.apipractice.features.coroutine.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_coroutine.*
import net.jeremystevens.apipractice.R
import javax.inject.Inject

class CoroutineFragment : Fragment(), Coroutine.View {

    @Inject
    lateinit var presenter: Coroutine.Presenter

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
    }

    override fun onDestroyView() {
        presenter.detach()
        super.onDestroyView()
    }

    override fun display(model: Coroutine.ViewModel) {
        when (model) {
            is Coroutine.ViewModel.Loading -> {
                addButton.visibility = View.INVISIBLE
                progressSpinner.visibility = View.VISIBLE
            }
            is Coroutine.ViewModel.DataModel -> {
                addButton.visibility = View.VISIBLE
                progressSpinner.visibility = View.INVISIBLE
                adapter.setDataset(model.people)
            }
        }
    }

    override fun showError(model: Coroutine.ErrorModel) {
        when (model) {
            is Coroutine.ErrorModel.NoMoreAvailable ->
                Toast.makeText(context, "No more entries available", Toast.LENGTH_SHORT).show()

            is Coroutine.ErrorModel.FailedToFetch ->
                Toast.makeText(context, "Failed to fetch new entry", Toast.LENGTH_SHORT).show()

            is Coroutine.ErrorModel.NetworkError ->
                Toast.makeText(context, "Network error: ${model.errorCode}", Toast.LENGTH_SHORT).show()
        }
    }
}
