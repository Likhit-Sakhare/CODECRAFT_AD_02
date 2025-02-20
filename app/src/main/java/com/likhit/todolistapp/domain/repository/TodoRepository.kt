package com.likhit.todolistapp.domain.repository

import com.likhit.todolistapp.domain.model.Todo
import com.likhit.todolistapp.domain.utils.TodoOrder
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getTodos(todoOrder: TodoOrder): Flow<List<Todo>>

    suspend fun getTodoById(id: Int): Todo?

    suspend fun upsertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)
}