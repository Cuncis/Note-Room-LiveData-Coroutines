package com.cuncis.noteapp.persistance

import androidx.lifecycle.LiveData
import com.cuncis.noteapp.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getNotes()
    }

    fun getNoteByTitle(title: String): LiveData<Note> {
        return noteDao.getNoteByTitle(title)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

}