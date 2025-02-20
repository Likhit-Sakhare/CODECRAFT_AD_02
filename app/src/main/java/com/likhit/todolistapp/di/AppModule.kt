package com.likhit.todolistapp.di

import android.app.Application
import androidx.room.Room
import com.likhit.todolistapp.data.database.TodoDatabase
import com.likhit.todolistapp.data.repository.TodoRepositoryImpl
import com.likhit.todolistapp.domain.repository.TodoRepository
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
    fun provideTodoDatabase(app: Application): TodoDatabase{
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            TodoDatabase.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository{
        return TodoRepositoryImpl(db.todoDao)
    }
}