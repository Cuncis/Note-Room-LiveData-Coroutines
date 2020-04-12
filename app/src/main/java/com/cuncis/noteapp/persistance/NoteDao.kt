package com.cuncis.noteapp.persistance

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cuncis.noteapp.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("SELECT * FROM notes_table")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE title LIKE :title")
    fun getNoteByTitle(title: String): LiveData<Note>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

}