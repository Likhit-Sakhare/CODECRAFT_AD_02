package com.likhit.todolistapp.presentation.todo_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.likhit.todolistapp.domain.utils.OrderType
import com.likhit.todolistapp.domain.utils.TodoOrder

@Composable
fun TodoOrderSection(
    modifier: Modifier = Modifier,
    onOrderChange: (TodoOrder) -> Unit,
    todoOrder: TodoOrder = TodoOrder.Date(OrderType.Descending)
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TodoRadioButton(
                text = "Title",
                selected = todoOrder is TodoOrder.Title,
                onSelected = {
                    onOrderChange(TodoOrder.Title(todoOrder.orderType))
                }
            )
            TodoRadioButton(
                text = "Date",
                selected = todoOrder is TodoOrder.Date,
                onSelected = {
                    onOrderChange(TodoOrder.Date(todoOrder.orderType))
                }
            )
            TodoRadioButton(
                text = "Color",
                selected = todoOrder is TodoOrder.Color,
                onSelected = {
                    onOrderChange(TodoOrder.Color(todoOrder.orderType))
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TodoRadioButton(
                text = "Ascending",
                selected = todoOrder.orderType is OrderType.Ascending,
                onSelected = {
                    onOrderChange(todoOrder.copy(OrderType.Ascending))
                }
            )
            TodoRadioButton(
                text = "Descending",
                selected = todoOrder.orderType is OrderType.Descending,
                onSelected = {
                    onOrderChange(todoOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}