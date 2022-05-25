package com.bytezeroone.noteapp.domain.repository

import androidx.room.Delete
import com.bytezeroone.noteapp.data.local.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    suspend fun getNoteById(id: Int): Note

    fun getNotes(): Flow<List<Note>>
}