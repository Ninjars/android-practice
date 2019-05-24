package net.jeremystevens.apipractice.features.starwars.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.jeremystevens.apipractice.R
import net.jeremystevens.apipractice.features.starwars.domain.PersonData

class PersonAdapter : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    private var data: MutableList<PersonData> = ArrayList()

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
        val diff = DiffUtil.calculateDiff(GenericDiffUtilCallback(this.data, data))

        this.data.clear()
        this.data.addAll(data)

        diff.dispatchUpdatesTo(this)
    }
}

class GenericDiffUtilCallback<T>(private val a: List<T>, private val b: List<T>) : DiffUtil.Callback() {
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
