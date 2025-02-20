@file:OptIn(ExperimentalMaterial3Api::class)

package com.likhit.todolistapp.presentation.todo_list.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.likhit.todolistapp.domain.model.Todo
import com.likhit.todolistapp.domain.utils.formattedTimeStamp

@Composable
fun TodoItem(
    modifier: Modifier = Modifier,
    todo: Todo,
    onCheckedChanged: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it){
                SwipeToDismissBoxValue.StartToEnd -> onDelete()
                SwipeToDismissBoxValue.Settled, SwipeToDismissBoxValue.EndToStart ->
                    return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier
            .fillMaxWidth(),
        backgroundContent = {
            DismissBackground(dismissState, todo.color)
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmileyCheckBox(
                    isChecked = todo.isDone,
                    onCheckedChanged = { isChecked ->
                        onCheckedChanged(isChecked)
                    },
                    modifier = Modifier.padding(4.dp)
                )
                Spacer(Modifier.width(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todo.title,
                        fontSize = 20.sp,
                        textDecoration = if(todo.isDone) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.weight(2f),
                        color = if(todo.isDone) Color.Gray else if(isSystemInDarkTheme()) Color.White else Color.Black
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "created at",
                            fontSize = 10.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = formattedTimeStamp(todo.timeStamp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DismissBackground(
    dismissState: SwipeToDismissBoxState,
    todoColor: Int
) {
    val isSwiping = dismissState.dismissDirection != SwipeToDismissBoxValue.Settled

    val swipeProgress = animateFloatAsState(
        targetValue = if(isSwiping)
            dismissState.requireOffset() else 0f,
        label = "swipeProgress"
    )

    val color = when(dismissState.dismissDirection){
        SwipeToDismissBoxValue.StartToEnd, SwipeToDismissBoxValue.EndToStart -> Color.Red
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(todoColor))
    ){
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(
                    with(LocalDensity.current){
                        swipeProgress.value.toDp()
                    }
                )
                .background(color)
                .padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if(isSwiping){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete todo"
                )
            }
        }
    }
}