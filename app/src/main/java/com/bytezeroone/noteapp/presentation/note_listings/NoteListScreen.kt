package com.bytezeroone.noteapp.presentation.note_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bytezeroone.noteapp.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun NoteListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val notes = viewModel.notes.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect() { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NoteListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(NoteListEvent.OnAddNoteClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(notes.value) { note ->
                NoteItem(
                    note = note,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(NoteListEvent.OnNoteClick(note))
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}