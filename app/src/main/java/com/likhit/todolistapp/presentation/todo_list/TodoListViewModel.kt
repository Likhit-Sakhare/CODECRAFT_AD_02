package com.likhit.todolistapp.presentation.todo_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likhit.todolistapp.domain.model.Todo
import com.likhit.todolistapp.domain.repository.TodoRepository
import com.likhit.todolistapp.domain.utils.OrderType
import com.likhit.todolistapp.domain.utils.TodoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos = _todos.asStateFlow()

    private val _todoOrder = MutableStateFlow<TodoOrder>(TodoOrder.Date(OrderType.Descending))
    val todoOrder = _todoOrder.asStateFlow()

    private val _isOrderSectionVisible = MutableStateFlow(false)
    val isOrderSectionVisible = _isOrderSectionVisible.asStateFlow()

    private var recentlyDeletedTodo: Todo? = null

    private var getTodosJob: Job? = null

    init {
        getTodos(TodoOrder.Date(OrderType.Descending))
    }

    private fun getTodos(todoOrder: TodoOrder) {
        getTodosJob?.cancel()
        getTodosJob = todoRepository.getTodos(todoOrder)
            .onEach { todos: List<Todo> ->
                _todos.value = todos
                _todoOrder.value = todoOrder
            }.launchIn(viewModelScope)
    }

    fun onOrderClick(todoOrder: TodoOrder) {
        //If the same todoOrder is clicked, do nothing
        if (_todoOrder.value::class == todoOrder::class &&
            _todoOrder.value.orderType == todoOrder.orderType
        ) {
            return
        }
        getTodos(todoOrder)
    }

    fun onDeleteClick(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
            recentlyDeletedTodo = todo
        }
    }

    fun onRestoreClick() {
        viewModelScope.launch {
            todoRepository.upsertTodo(recentlyDeletedTodo ?: return@launch)
            recentlyDeletedTodo = null
        }
    }

    fun onToggleClick() {
        _isOrderSectionVisible.value = !_isOrderSectionVisible.value
    }

    fun onDoneChange(
        todo: Todo,
        isDone: Boolean,
    ) {
        viewModelScope.launch {
            todoRepository.upsertTodo(
                todo.copy(
                    isDone = isDone
                )
            )
        }
    }
}