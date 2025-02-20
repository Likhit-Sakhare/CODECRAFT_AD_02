package com.likhit.todolistapp.presentation.todo_list.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TodoRadioButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onSelected()
            }
        )
        Text(
            text = text
        )
    }
}