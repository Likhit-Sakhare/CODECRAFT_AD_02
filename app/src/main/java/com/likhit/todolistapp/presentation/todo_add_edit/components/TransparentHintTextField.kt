package com.likhit.todolistapp.presentation.todo_add_edit.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization

@Composable
fun TransparentHintTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean = false,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = true,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit
) {
    val textColor = if(isSystemInDarkTheme()) Color.White else Color.Black
    Box(
        modifier = modifier
            .pointerInput(Unit){
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            }
    ){
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = textStyle.copy(color = textColor),
            cursorBrush = if(isSystemInDarkTheme()) SolidColor(Color.White)
                            else SolidColor(Color.Black),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
        )
        if(isHintVisible){
            Text(
                text = hint,
                style = textStyle,
                color = if(isSystemInDarkTheme()) Color.White.copy(alpha = 0.9f) else Color.DarkGray
            )
        }
    }
}