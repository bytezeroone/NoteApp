package com.bytezeroone.noteapp.presentation.note_listings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ExpandCircleDown
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bytezeroone.noteapp.data.local.Note

@Composable
fun NoteItem(
    note: Note,
    onEvent: (NoteListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(note.description) }

    val textLayoutResult = textLayoutResultState.value
    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                finalText = "${note.description}"
            }
            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                isClickable = true
            }
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = note.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    //Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            onEvent(NoteListEvent.OnDeleteNoteClick(note))
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }

                }
            }
            Divider()
            finalText.let { it ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (isClickable) {
                        true -> Icon(
                            imageVector = (
                                    (if (isExpanded) {
                                        Icons.Outlined.ExpandLess
                                    } else if (!isExpanded && isClickable) {
                                        Icons.Outlined.ExpandMore
                                    } else Unit) as ImageVector
                                    ),
                            contentDescription = "show more",
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                        false -> Unit
                    }
                    if (it != null) {
                        Text(
                            text = it,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                            onTextLayout = { textLayoutResultState.value = it },
                            modifier = modifier
                                .clickable(enabled = isClickable) { isExpanded = !isExpanded }
                                .animateContentSize(),
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                }
            }
        }
    }
}