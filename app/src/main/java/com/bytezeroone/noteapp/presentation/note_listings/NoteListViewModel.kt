package com.bytezeroone.noteapp.presentation.note_listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytezeroone.noteapp.data.local.Note
import com.bytezeroone.noteapp.domain.repository.NoteRepository
import com.bytezeroone.noteapp.util.Routes
import com.bytezeroone.noteapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository
): ViewModel() {

    val notes = repository.getNotes()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedNote: Note? = null


    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnNoteClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_NOTE + "?noteId=${event.note.id}"))
            }
            is NoteListEvent.OnAddNoteClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_NOTE))
            }
            is NoteListEvent.OnUndoDeleteClick -> {
                deletedNote?.let { note ->
                    viewModelScope.launch {
                        repository.insertNote(note)
                    }
                }
            }
            is NoteListEvent.OnDeleteNoteClick -> {
                viewModelScope.launch {
                    deletedNote = event.note
                    repository.deleteNote(event.note)
                    sendUiEvent(UiEvent.ShowSnackBar(
                        message = "Note deleted",
                        action = "Undo"
                    ))
                }
            }
        }
    }


    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}