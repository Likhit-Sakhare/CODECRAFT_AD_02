package com.likhit.todolistapp.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.likhit.todolistapp.ui.theme.BabyBlue
import com.likhit.todolistapp.ui.theme.RedOrange
import com.likhit.todolistapp.ui.theme.RedPink
import com.likhit.todolistapp.ui.theme.Violet

@Entity
data class Todo(
    val title: String,
    val description: String?,
    val timeStamp: Long,
    val color: Int,
    val isDone: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
){
    companion object{
        val todoColors = listOf(
            Color.Transparent,
            RedOrange,
            BabyBlue,
            Violet,
            RedPink
        )
    }
}

class InvalidTodoException(message: String): Exception(message)
