package com.likhit.todolistapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.likhit.todolistapp.domain.model.Todo

@Database(
    entities = [Todo::class],
    version = 1
)
abstract class TodoDatabase: RoomDatabase() {
    abstract val todoDao: TodoDao
    companion object {
        const val DB_NAME = "todo_db"
    }
}