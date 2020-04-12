package com.cuncis.noteapp

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.cuncis.noteapp.model.Note
import com.cuncis.noteapp.util.Constant.EDIT_MODE_DISABLED
import com.cuncis.noteapp.util.Constant.EDIT_MODE_ENABLED
import com.cuncis.noteapp.util.Helper
import com.cuncis.noteapp.util.Utility
import com.cuncis.noteapp.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.layout_note_toolbar.*

class NoteActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener,
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, TextWatcher {

    // vars
    private lateinit var gestureDetector: GestureDetector
    private var isNewNote: Boolean = false
    private lateinit var note: Note
    private lateinit var finalNote: Note
    private var mode: Int = 0

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        if (getIncomingIntent()) {
            // Edit Mode
            setNewNoteProperties()
            enableEditMode()
        } else {
            // New Note
            setNoteProperties()
            disableContentInteraction()
        }

        setListener()
    }

    private fun getIncomingIntent(): Boolean {
        if (intent.hasExtra("SELECTED_NOTE")) {
            note = intent.getParcelableExtra("SELECTED_NOTE") as Note

            finalNote = Note()
            finalNote.id = note.id
            finalNote.title = note.title
            finalNote.content = note.content
            finalNote.timestamp = note.timestamp

            mode = EDIT_MODE_DISABLED
            isNewNote = false
            return false
        }

        mode = EDIT_MODE_ENABLED
        isNewNote = true
        return true
    }

    private fun saveChanges() {
        if (isNewNote) {
            saveNewNote()
        } else {
            updateNote()
        }
    }

    private fun updateNote() {
        noteViewModel.update(finalNote)
    }

    private fun saveNewNote() {
        noteViewModel.insert(finalNote)
    }

    private fun disableContentInteraction() {
        note_text.keyListener = null
        note_text.isFocusable = false
        note_text.isFocusableInTouchMode = false
        note_text.isCursorVisible = false
        note_text.clearFocus()
    }

    private fun enableContentInteraction() {
        note_text.keyListener = EditText(this).keyListener
        note_text.isFocusable = true
        note_text.isFocusableInTouchMode = true
        note_text.isCursorVisible = true
        note_text.requestFocus()
    }

    private fun enableEditMode() {
        back_arrow_container.visibility = View.GONE
        check_container.visibility = View.VISIBLE

        note_text_title.visibility = View.GONE
        note_edit_title.visibility = View.VISIBLE

        mode = EDIT_MODE_ENABLED

        enableContentInteraction()
    }

    private fun disableEditMode() {
        back_arrow_container.visibility = View.VISIBLE
        check_container.visibility = View.GONE

        note_text_title.visibility = View.VISIBLE
        note_edit_title.visibility = View.GONE

        mode = EDIT_MODE_DISABLED

        disableContentInteraction()

        var temp = note_text.text.toString()
        temp = temp.replace("\n", "")
        temp = temp.replace(" ", "")
        if (temp.isNotEmpty()) {
            finalNote.title = note_edit_title.text.toString()
            finalNote.content = note_text.text.toString()
            val timestamp = Utility.getCurrentTime()
            finalNote.timestamp = timestamp

            if ((finalNote.content != note.content) or (finalNote.title != note.title)) {
                saveChanges()
            }
        }
    }

    private fun setListener() {
        note_text.setOnTouchListener(this)
        gestureDetector = GestureDetector(this, this)
        note_text_title.setOnClickListener(this)
        toolbar_check.setOnClickListener(this)
        toolbar_back_arrow.setOnClickListener(this)
        note_edit_title.addTextChangedListener(this)
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setNoteProperties() {
        note_text_title.text = note.title
        note_edit_title.setText(note.title)
        note_text.setText(note.content)
    }

    private fun setNewNoteProperties() {
        note_text_title.text = "Note Title"
        note_edit_title.setText("Note Title")

        note = Note()
        finalNote = Note()
        note.title = "Note Title"
        finalNote.title = "Note Title"
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Helper.showLog("Double Click Clicked")
        enableEditMode()
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_check -> {
                hideSoftKeyboard()
                disableEditMode()
            }
            R.id.note_text_title -> {
                enableEditMode()
                note_edit_title.requestFocus()
                note_edit_title.setSelection(note_edit_title.length())
            }
            R.id.toolbar_back_arrow -> {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (mode == EDIT_MODE_ENABLED) {
            onClick(toolbar_check)
        } else {
            super.onBackPressed()
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        note_text_title.text = s.toString()
    }
}
