package com.bytezeroone.noteapp.presentation.edit_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytezeroone.noteapp.data.local.Note
import com.bytezeroone.noteapp.domain.repository.NoteRepository
import com.bytezeroone.noteapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var note by mutableStateOf<Note?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val noteId = savedStateHandle.get<Int>("noteId")
        if (noteId != -1) {
            viewModelScope.launch {
                repository.getNoteById(noteId!!).let { note ->
                    title = note.title
                    description = note.description ?: ""
                    this@EditNoteViewModel.note = note
                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {
        when (event) {
            is EditNoteEvent.OnTitleChange -> {
                title = event.title
            }
            is EditNoteEvent.OnDescriptionChange -> {
                description = event.description
            }
            is EditNoteEvent.OnSaveNoteClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackBar(
                            message = "The title can't be empty"
                        ))
                        return@launch
                    }
                    repository.insertNote(
                        Note(
                            title = title,
                            description = description,
                            id = note?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
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