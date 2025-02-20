package com.likhit.todolistapp.presentation.todo_list.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmileyCheckBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .border(
                width = 2.dp,
                color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                onCheckedChanged(!isChecked)
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = if(isChecked) "👍🏻" else "", //\uD83D\uDE0A
            fontSize = 20.sp,
            fontFamily = FontFamily.Default
        )
    }
}