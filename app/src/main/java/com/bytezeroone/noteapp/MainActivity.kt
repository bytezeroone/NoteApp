package com.bytezeroone.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bytezeroone.noteapp.presentation.edit_note.AddEditNoteScreen
import com.bytezeroone.noteapp.presentation.note_listings.NoteListScreen
import com.bytezeroone.noteapp.ui.theme.NoteAppTheme
import com.bytezeroone.noteapp.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.NOTE_LIST
                    ) {
                        composable(Routes.NOTE_LIST) {
                            NoteListScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                        composable(
                            route = Routes.ADD_EDIT_NOTE + "?noteId={noteId}",
                            arguments = listOf(
                                navArgument(name = "noteId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                            ) {
                            AddEditNoteScreen(onPopBackStack = {
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}
