package com.likhit.todolistapp.presentation.todo_add_edit

sealed class AddEditTodoEvent {
    data class ShowToast(val message: String): AddEditTodoEvent()
    data object SaveTodo: AddEditTodoEvent()
}