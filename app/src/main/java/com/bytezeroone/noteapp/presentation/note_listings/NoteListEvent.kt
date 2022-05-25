package com.bytezeroone.noteapp.presentation.note_listings

import com.bytezeroone.noteapp.data.local.Note

sealed class NoteListEvent {
    data class OnDeleteNoteClick(val note: Note): NoteListEvent()
    object OnUndoDeleteClick: NoteListEvent()
    data class OnNoteClick(val note: Note): NoteListEvent()
    object OnAddNoteClick: NoteListEvent()
}
