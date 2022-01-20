package android.sample.com.noteapp.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Filter
import android.widget.Filterable
import java.util.*
import kotlin.collections.ArrayList

import android.sample.com.noteapp.Constants.DATE_FORMAT
import android.sample.com.noteapp.db.Note
import android.sample.com.noteapp.databinding.NoteItemBinding


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(), Filterable {

    private var data: ArrayList<Note> = ArrayList()
    private var dataSearch: ArrayList<Note>
    private lateinit var editListener: OnEditClickListener
    private lateinit var shareListener: OnShareClickListener
    private lateinit var deleteListener: OnDeleteClickListener

    init {
        dataSearch = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val noteBinding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        editListener = parent.context as MainActivity
        shareListener = parent.context as MainActivity
        deleteListener = parent.context as MainActivity

        return NoteViewHolder(noteBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = dataSearch[position]
        holder.bindTo(note)
    }

    override fun getItemCount(): Int {
        return dataSearch.size
    }

    fun setData(newData: List<Note>) {
        if(data.isEmpty()){
            data = newData as ArrayList
        } else {
            data.clear()
            data.addAll(newData)
        }
        dataSearch = data
    }

    fun sort() {
        dataSearch.sortWith(Comparator { one, two ->
            when {
                DATE_FORMAT.parse(one.date) > DATE_FORMAT.parse(two.date) -> 1
                DATE_FORMAT.parse(one.date) < DATE_FORMAT.parse(two.date) -> -1
                else -> 0
            }
        })
    }

    fun reverse() {
        dataSearch.sortWith(Comparator { one, two ->
            when {
                DATE_FORMAT.parse(one.date) < DATE_FORMAT.parse(two.date) -> 1
                DATE_FORMAT.parse(one.date) > DATE_FORMAT.parse(two.date) -> -1
                else -> 0
            }
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                dataSearch = results.values as ArrayList<Note>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredResults: ArrayList<Note> = if (constraint.isEmpty()) {
                    data
                } else {
                    getFilteredResults(constraint.toString().toLowerCase())
                }

                val results = FilterResults()
                results.values = filteredResults

                return results
            }
        }
    }

    private fun getFilteredResults(constraint: String): ArrayList<Note> {
        val results = ArrayList<Note>()
        for (item in data) {
            if (item.text.contains(constraint)) {
                results.add(item)
            }
        }
        return results
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root){
        lateinit var note : Note
        fun bindTo(note : Note) {
            this.note = note

            binding.date.text = note.date
            binding.content.text = note.text

            binding.edit.setOnClickListener { editListener.onEditClicked(note) }
            binding.share.setOnClickListener { shareListener.onShareClicked(note) }
            binding.delete.setOnClickListener { deleteListener.onDeleteClicked(note) }
        }
    }
}