package com.likhit.todolistapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.likhit.todolistapp.presentation.todo_add_edit.AddEditTodoScreenRoot
import com.likhit.todolistapp.presentation.todo_list.TodoListScreenRoot

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "todo_list"
    ) {
        composable(
            route = "todo_list"
        ){
            TodoListScreenRoot(
                onAddClick = {
                    navController.navigate("add_edit_todo")
                },
                onTodoClick = { id, color ->
                    navController.navigate(
                        "add_edit_todo" + "?todoId=${id}&todoColor=${color}"
                    )
                }
            )
        }
        composable(
            route = "add_edit_todo" + "?todoId={todoId}&todoColor={todoColor}",
            arguments = listOf(
                navArgument(
                    name = "todoId"
                ){
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "todoColor"
                ){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            val color = it.arguments?.getInt("todoColor")?: -1
            AddEditTodoScreenRoot(
                todoColor = color,
                onSaveClick = {
                    navController.navigateUp()
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}