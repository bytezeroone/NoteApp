package com.bytezeroone.noteapp.di

import android.app.Application
import androidx.room.Room
import com.bytezeroone.noteapp.data.local.NoteDatabase
import com.bytezeroone.noteapp.domain.repository.NoteRepository
import com.bytezeroone.noteapp.data.repository.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "note_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.dao)
    }
}