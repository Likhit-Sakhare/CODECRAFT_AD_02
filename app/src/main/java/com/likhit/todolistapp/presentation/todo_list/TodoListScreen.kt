package com.likhit.todolistapp.presentation.todo_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.likhit.todolistapp.domain.model.Todo
import com.likhit.todolistapp.domain.utils.TodoOrder
import com.likhit.todolistapp.presentation.todo_list.components.TodoItem
import com.likhit.todolistapp.presentation.todo_list.components.TodoOrderSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TodoListScreenRoot(
    viewModel: TodoListViewModel = hiltViewModel(),
    onAddClick: () -> Unit,
    onTodoClick: (Int, Int) -> Unit
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    val todos = viewModel.todos.collectAsState()
    val todoOrder = viewModel.todoOrder.collectAsState()
    val isOrderSectionVisible = viewModel.isOrderSectionVisible.collectAsState()

    TodoListScreen(
        isOrderSectionVisible = isOrderSectionVisible.value,
        todoOrder = todoOrder.value,
        todos = todos.value,
        onAddClick = onAddClick,
        onToggleClick = viewModel::onToggleClick,
        onOrderClick = viewModel::onOrderClick,
        onTodoClick = onTodoClick,
        onDeleteClick = viewModel::onDeleteClick,
        onRestoreClick = viewModel::onRestoreClick,
        onDoneChange = viewModel::onDoneChange,
        snackbarHostState = snackbarHostState,
        scope = scope
    )
}

@Composable
fun TodoListScreen(
    modifier: Modifier = Modifier,
    isOrderSectionVisible: Boolean,
    todoOrder: TodoOrder,
    todos: List<Todo>,
    onAddClick: () -> Unit,
    onToggleClick: () -> Unit,
    onOrderClick: (TodoOrder) -> Unit,
    onTodoClick: (Int, Int) -> Unit,
    onDeleteClick: (Todo) -> Unit,
    onRestoreClick: () -> Unit,
    onDoneChange: (Todo, Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a todo"
                )
            }
        },
        modifier = modifier
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Todos",
                    fontSize = 24.sp
                )
                IconButton(
                    onClick = {
                        onToggleClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort todos"
                    )
                }
            }
            AnimatedVisibility(
                visible = isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                TodoOrderSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    todoOrder = todoOrder,
                    onOrderChange = {
                        onOrderClick(it)
                    }
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(todos, key = { it.id!! }) { todo ->
                    TodoItem(
                        todo = todo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTodoClick(todo.id!!, todo.color)
                            },
                        onDelete = {
                            onDeleteClick(todo)
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Todo Deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                                if(result == SnackbarResult.ActionPerformed){
                                    onRestoreClick()
                                }
                            }
                        },
                        onCheckedChanged = {
                            onDoneChange(todo, it)
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}