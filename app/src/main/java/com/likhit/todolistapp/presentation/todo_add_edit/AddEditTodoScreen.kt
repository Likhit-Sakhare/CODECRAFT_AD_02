package com.likhit.todolistapp.presentation.todo_add_edit

import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.likhit.todolistapp.R
import com.likhit.todolistapp.domain.model.Todo
import com.likhit.todolistapp.presentation.todo_add_edit.components.TransparentHintTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditTodoScreenRoot(
    viewModel: AddEditTodoViewModel = hiltViewModel(),
    todoColor: Int,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val titleText = viewModel.titleText.collectAsState()
    val titleHint = viewModel.titleHint.collectAsState()
    val descriptionText = viewModel.descriptionText.collectAsState()
    val descriptionHint = viewModel.descriptionHint.collectAsState()
    val isTitleHintVisible = viewModel.isTitleHintVisible.collectAsState()
    val isDescriptionHintVisible = viewModel.isDescriptionHintVisible.collectAsState()
    val todoColorFromVM = viewModel.todoColor.collectAsState()
    val isColorBarVisible = viewModel.isColorBarVisible.collectAsState()

    AddEditTodoScreen(
        todoColor = todoColor,
        todoColorFromVM = todoColorFromVM.value,
        titleText = titleText.value,
        titleHint = titleHint.value,
        descriptionText = descriptionText.value,
        descriptionHint = descriptionHint.value,
        isTitleHintVisible = isTitleHintVisible.value,
        isDescriptionHintVisible = isDescriptionHintVisible.value,
        isColorBarVisible = isColorBarVisible.value,
        onEnteredTitle = viewModel::onEnteredTitle,
        onTitleFocusChange = viewModel::onChangeTitleFocus,
        onEnteredDescription = viewModel::onEnteredDescription,
        onDescriptionFocusChange = viewModel::onChangedDescriptionFocus,
        onSaveTodo = viewModel::onSaveTodo,
        onColorBarToggleClick = viewModel::onColorBarToggleClick,
        onColorChange = viewModel::onColorChange,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState,
        keyboardController = keyboardController,
        focusManager = focusManager,
        scope = scope
    )

    val events = viewModel.events
    LaunchedEffect(true) {
        events.collectLatest { event ->
            when(event){
                AddEditTodoEvent.SaveTodo -> onSaveClick()
                is AddEditTodoEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

@Composable
fun AddEditTodoScreen(
    modifier: Modifier = Modifier,
    todoColor: Int,
    todoColorFromVM: Int,
    titleText: String,
    titleHint: String,
    descriptionText: String,
    descriptionHint: String,
    isDescriptionHintVisible: Boolean,
    isTitleHintVisible: Boolean,
    isColorBarVisible: Boolean,
    onEnteredTitle: (String) -> Unit,
    onTitleFocusChange: (FocusState) -> Unit,
    onEnteredDescription: (String) -> Unit,
    onDescriptionFocusChange: (FocusState) -> Unit,
    onSaveTodo: () -> Unit,
    onColorBarToggleClick: () -> Unit,
    onColorChange: (Int) -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    scope: CoroutineScope
) {
    val todoBackgroundAnimatable = remember {
        Animatable(
            Color(if (todoColor != -1) todoColor else todoColorFromVM)
        )
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSaveTodo
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save a todo"
                )
            }
        },
        modifier = modifier
    ){ _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(todoBackgroundAnimatable.value)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .shadow(15.dp, CircleShape)
                        .border(
                            width = 1.5.dp,
                            color = if(isSystemInDarkTheme())
                                Color.White.copy(alpha = 0.5f) else
                                    Color.Black.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                ){
                    IconButton(
                        onClick = {
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .shadow(15.dp, CircleShape)
                        .border(
                            width = 1.5.dp,
                            color = if(isSystemInDarkTheme())
                                Color.White.copy(alpha = 0.5f) else
                                Color.Black.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                ){
                    IconButton(
                        onClick = {
                            onColorBarToggleClick()
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.paint_board),
                            contentDescription = "Color Bar"
                        )
                    }
                }
            }
            HorizontalDivider(
                thickness = 0.3.dp,
                color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            AnimatedVisibility(
                visible = isColorBarVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Todo.todoColors.forEach { color ->
                        val colorInt = color.toArgb()

                        if(color == Color.Transparent){
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .shadow(elevation = 15.dp, shape = CircleShape)
                                    .background(color)
                                    .border(
                                        width = 2.dp,
                                        color = if(todoColorFromVM == colorInt){
                                            if(isSystemInDarkTheme()){
                                                Color.White
                                            }else{
                                                Color.Black
                                            }
                                        }else{
                                            Color.Transparent
                                        },
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        scope.launch {
                                            todoBackgroundAnimatable.animateTo(
                                                targetValue = Color(colorInt),
                                                animationSpec = tween(
                                                    durationMillis = 500
                                                )
                                            )
                                        }
                                        onColorChange(colorInt)
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Image(
                                    painter = painterResource(R.drawable.remove),
                                    contentDescription = "No color",
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }else{
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .shadow(15.dp, CircleShape)
                                    .background(color)
                                    .border(
                                        width = 2.dp,
                                        color = if(todoColorFromVM == colorInt){
                                            if(isSystemInDarkTheme()){
                                                Color.White
                                            }else{
                                                Color.Black
                                            }
                                        }else{
                                            Color.Transparent
                                        },
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        scope.launch {
                                            todoBackgroundAnimatable.animateTo(
                                                targetValue = Color(colorInt),
                                                animationSpec = tween(
                                                    durationMillis = 500
                                                )
                                            )
                                        }
                                        onColorChange(colorInt)
                                    }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            TransparentHintTextField(
                text = titleText,
                hint = titleHint,
                onValueChange = {
                    onEnteredTitle(it)
                },
                onFocusChange = {
                    onTitleFocusChange(it)
                },
                isHintVisible = isTitleHintVisible,
                singleLine = false,
                keyboardController = keyboardController,
                focusManager = focusManager,
                textStyle = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = descriptionText,
                hint = descriptionHint,
                onValueChange = {
                    onEnteredDescription(it)
                },
                onFocusChange = {
                    onDescriptionFocusChange(it)
                },
                isHintVisible = isDescriptionHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardController = keyboardController,
                focusManager = focusManager,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}