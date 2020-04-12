package com.cuncis.noteapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuncis.noteapp.R
import com.cuncis.noteapp.model.Note
import com.cuncis.noteapp.util.Utility
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(private val context: Context): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var noteList: List<Note> = arrayListOf()
    lateinit var noteClickListener: OnNoteClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false))
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])
    }

    fun setNoteList(noteList: List<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }

    fun setNoteListener(noteClickListener: OnNoteClickListener) {
        this.noteClickListener = noteClickListener
    }

    inner class NoteViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private val title: TextView = view.note_title
        private val timestamp: TextView = view.note_timestamp

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(note: Note) {
            title.text = note.title
            timestamp.text = note.timestamp
        }

        override fun onClick(v: View?) {
            noteClickListener.onNoteClick(adapterPosition)
        }
    }

    interface OnNoteClickListener {
        fun onNoteClick(position: Int)
    }
}