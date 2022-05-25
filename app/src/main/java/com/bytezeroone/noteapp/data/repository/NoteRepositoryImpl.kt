package com.bytezeroone.noteapp.data.repository

import com.bytezeroone.noteapp.data.local.Note
import com.bytezeroone.noteapp.data.local.NoteDao
import com.bytezeroone.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
): NoteRepository {
    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    override suspend fun getNoteById(id: Int): Note {
        return dao.getNoteById(id)
    }

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }
}