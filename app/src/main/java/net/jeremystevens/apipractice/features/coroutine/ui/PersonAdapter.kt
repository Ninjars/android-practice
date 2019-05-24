package net.jeremystevens.apipractice.features.coroutine.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.jeremystevens.apipractice.R
import net.jeremystevens.apipractice.features.coroutine.domain.PersonData
import timber.log.Timber

class PersonAdapter : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    private var data: List<PersonData> = emptyList()

    class PersonViewHolder(private val view: ViewGroup) : RecyclerView.ViewHolder(view) {
        fun setData(data: PersonData) {
            view.findViewById<TextView>(R.id.title).text = data.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_person, parent, false) as ViewGroup
        return PersonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.setData(data[position])
    }

    fun setDataset(data: List<PersonData>) {
        Timber.i("setDataset $data")
//        val diff = DiffUtil.calculateDiff(Differentiator(this.data, data))
        this.data = data

//        diff.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}

class Differentiator<T>(private val a: List<T>, private val b: List<T>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return a[oldItemPosition] == b[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return a.size
    }

    override fun getNewListSize(): Int {
        return b.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return a[oldItemPosition] == b[newItemPosition]
    }
}
