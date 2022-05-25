package com.bytezeroone.noteapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val description: String?,
    @PrimaryKey val id: Int? = null
)