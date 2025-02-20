package com.likhit.todolistapp.presentation.todo_add_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likhit.todolistapp.domain.model.InvalidTodoException
import com.likhit.todolistapp.domain.model.Todo
import com.likhit.todolistapp.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    private val _titleText = MutableStateFlow("")
    val titleText = _titleText.asStateFlow()

    private val _descriptionText = MutableStateFlow("")
    val descriptionText = _descriptionText.asStateFlow()

    private val _titleHint = MutableStateFlow("Enter title...")
    val titleHint = _titleHint.asStateFlow()

    private val _descriptionHint = MutableStateFlow("Enter some description if you want...")
    val descriptionHint = _descriptionHint.asStateFlow()

    private val _isTitleHintVisible = MutableStateFlow(false)
    val isTitleHintVisible = _isTitleHintVisible.asStateFlow()

    private val _isDescriptionHintVisible = MutableStateFlow(false)
    val isDescriptionHintVisible = _isDescriptionHintVisible.asStateFlow()

    private val _todoColor = MutableStateFlow(Todo.todoColors.random().toArgb())
    val todoColor = _todoColor.asStateFlow()

    private val _isColoBarVisible = MutableStateFlow(false)
    val isColorBarVisible = _isColoBarVisible.asStateFlow()

    private val _eventChannel = Channel<AddEditTodoEvent>()
    val events = _eventChannel.receiveAsFlow()

    private var currentTodoId: Int? = null

    init {
        savedStateHandle.get<Int>("todoId")?.let { todoId ->
            if(todoId != -1){
                viewModelScope.launch {
                    todoRepository.getTodoById(todoId)?.also { todo ->
                        currentTodoId = todo.id
                        _titleText.value = todo.title
                        _descriptionText.value = todo.description?: ""
                        _isTitleHintVisible.value = false
                        _isDescriptionHintVisible.value = todo.description?.isBlank() == true
                        _todoColor.value = todo.color
                        this@AddEditTodoViewModel.todo = todo
                    }
                }
            }
        }
    }

    fun onEnteredTitle(title: String){
        _titleText.value = title
    }

    fun onChangeTitleFocus(titleFocusState: FocusState){
        _isTitleHintVisible.value = !titleFocusState.isFocused &&
                _titleText.value.isBlank()
    }

    fun onEnteredDescription(description: String){
        //Store the default value of the description text
        val currentText = _descriptionText.value

        // Check if the new text ends with new line i.e. "\n"
        if (description.endsWith("\n")) {
            //Split the input into individual lines and filter it so that there are no empty lines
            val lines = currentText.split("\n").filter { it.isNotEmpty() }

            // Extract the most recent line typed by the user
            val lastLine = lines.lastOrNull()
            //This is the pattern of our bullet point i.e. "1."
            val numberPattern = Regex("""^\d+\.""")

            //If the last line is not null and our number pattern is present in the last line
            //then we can do our work
            if (lastLine != null && numberPattern.containsMatchIn(lastLine)) {
                //First extract the current number present in that line i.e. "1." then drop last
                //from it i.e. drop "." and then convert it to an integer to be safe
                val currentNumber = numberPattern.find(lastLine)?.value?.dropLast(1)?.toIntOrNull()

                if (currentNumber != null) {
                    //If the current number is not null, then we will build a string which has our
                    //previous text i.e. currentText, then we go to next line and then we will
                    //append the next number after incrementing it and adding a dot
                    val newText = buildString {
                        append(currentText)
                        append("\n")
                        append("${currentNumber + 1}. ")
                    }
                    //Save our new text in the stateflow of descriptionText
                    _descriptionText.value = newText
                    return
                }
            }
        }

        // If conditions aren't met, just update with the new text
        _descriptionText.value = description
    }

    fun onChangedDescriptionFocus(descriptionFocus: FocusState){
        _isDescriptionHintVisible.value = !descriptionFocus.isFocused &&
                _descriptionText.value.isBlank()
    }

    fun onColorChange(color: Int){
        _todoColor.value = color
    }

    fun onColorBarToggleClick(){
        _isColoBarVisible.value = !_isColoBarVisible.value
    }

    fun onSaveTodo(){
        viewModelScope.launch {
            try {
                todoRepository.upsertTodo(
                    Todo(
                        title = _titleText.value,
                        description = _descriptionText.value,
                        timeStamp = System.currentTimeMillis(),
                        color = _todoColor.value,
                        isDone = todo?.isDone?: false,
                        id = currentTodoId
                    )
                )
                _eventChannel.send(AddEditTodoEvent.SaveTodo)
            }catch (e: InvalidTodoException){
                _eventChannel.send(
                    AddEditTodoEvent.ShowToast(
                        message = e.message?: "Couldn't save todo"
                    )
                )
            }
        }
    }
}