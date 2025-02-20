package com.likhit.todolistapp.data.repository

import com.likhit.todolistapp.data.database.TodoDao
import com.likhit.todolistapp.domain.model.InvalidTodoException
import com.likhit.todolistapp.domain.model.Todo
import com.likhit.todolistapp.domain.repository.TodoRepository
import com.likhit.todolistapp.domain.utils.OrderType
import com.likhit.todolistapp.domain.utils.TodoOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.jvm.Throws

class TodoRepositoryImpl(
    private val todoDao: TodoDao
): TodoRepository {
    override fun getTodos(todoOrder: TodoOrder): Flow<List<Todo>> {
        return todoDao.getTodos().map { todos ->
            when(todoOrder.orderType){
                OrderType.Ascending -> {
                    when(todoOrder){
                        is TodoOrder.Title -> todos.sortedBy { it.title.lowercase() }
                        is TodoOrder.Date -> todos.sortedBy { it.timeStamp }
                        is TodoOrder.Color -> todos.sortedBy { it.color }
                    }
                }
                OrderType.Descending -> {
                    when(todoOrder){
                        is TodoOrder.Title -> todos.sortedByDescending { it.title.lowercase() }
                        is TodoOrder.Date -> todos.sortedByDescending { it.timeStamp }
                        is TodoOrder.Color -> todos.sortedByDescending { it.color }
                    }
                }
            }
        }
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }

    @Throws(InvalidTodoException::class)
    override suspend fun upsertTodo(todo: Todo) {
        if(todo.title.isBlank()){
            throw InvalidTodoException("The title of the note can't be empty.")
        }
        return todoDao.upsertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        return todoDao.deleteTodo(todo)
    }
}