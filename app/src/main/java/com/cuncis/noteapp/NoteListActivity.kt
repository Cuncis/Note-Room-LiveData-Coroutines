package com.cuncis.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuncis.noteapp.adapter.NoteAdapter
import com.cuncis.noteapp.model.Note
import com.cuncis.noteapp.util.VerticalSpacingItemDecorator
import com.cuncis.noteapp.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_note_list.*

class NoteListActivity : AppCompatActivity(), NoteAdapter.OnNoteClickListener {

    private lateinit var noteAdapter: NoteAdapter
    private var noteList: ArrayList<Note> = ArrayList()
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        toolbar.title = "Note"

        noteAdapter = NoteAdapter(this)
        rv_note.layoutManager = LinearLayoutManager(this)
        rv_note.setHasFixedSize(true)
        noteAdapter.setNoteListener(this)
        rv_note.addItemDecoration(VerticalSpacingItemDecorator(10))
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_note)
        rv_note.adapter = noteAdapter

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this, Observer {
            noteList = it as ArrayList<Note>
            noteAdapter.setNoteList(it)
        })

        fab.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteNote(note: Note) {
        noteList.remove(note)
        noteAdapter.setNoteList(noteList)

        noteViewModel.delete(note)
    }

    private val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            deleteNote(noteList[viewHolder.adapterPosition])
        }

    }

    override fun onNoteClick(position: Int) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("SELECTED_NOTE", noteList[position])
        startActivity(intent)
    }
}
