package com.bytezeroone.noteapp.presentation.edit_note

sealed class EditNoteEvent {
    data class OnTitleChange(val title: String): EditNoteEvent()
    data class OnDescriptionChange(val description: String): EditNoteEvent()
    object OnSaveNoteClick: EditNoteEvent()
}
