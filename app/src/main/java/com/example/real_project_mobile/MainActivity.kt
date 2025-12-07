package com.example.real_project_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.real_project_mobile.ViewTodosScreen
import com.example.real_project_mobile.CreateTodoScreen
import com.example.real_project_mobile.data.AppDatabase
import com.example.real_project_mobile.data.Todo
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.example.real_project_mobile.CompletedTodosScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RealProjectMobileApp()
        }
    }
}

@Composable
fun RealProjectMobileApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val todoDao = remember { db.todoDao() }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            @Composable
            fun ToDoHomeScreen(
                onViewTodosClick: () -> Unit,
                onCreateTodoClick: () -> Unit
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ToDo App",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))



                    Image(
                        painter = painterResource(id = R.drawable.todoimg),
                        contentDescription = "ToDo illustration",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "Life’s busy enough. your to do list shouldn’t be. Our app helps you keep track of everything that matters, from daily errands to long-term goals, without the clutter or stress. Add tasks in seconds, set gentle reminders, and enjoy the simple satisfaction of checking things off. Stay organized, stay calm, and make space for what really matters.",
                        style = MaterialTheme.typography.bodyMedium
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onViewTodosClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Incomplete Todos")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { navController.navigate("completed_todos") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Completed Todos")
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onCreateTodoClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Todo")
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                // HOME
                composable("home") {
                    ToDoHomeScreen(
                        onViewTodosClick = { navController.navigate("view_todos") },
                        onCreateTodoClick = { navController.navigate("create_todo") }
                    )
                }

                // VIEW TODOS
                composable("view_todos") {
                    ViewTodosScreen(
                        onTodoClick = { todo: Todo -> navController.navigate("todo_detail/${todo.id}" )},
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable("completed_todos") {
                    CompletedTodosScreen(
                        onTodoClick = { todo ->
                            navController.navigate("todo_detail/${todo.id}")
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }



                composable("create_todo") {
                    CreateTodoScreen(
                        onSave = { todo ->
                            scope.launch {
                                todoDao.insertTodo(todo)
                            }
                            navController.popBackStack()
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("todo_detail/{todoId}") { backStackEntry ->
                    val todoId = backStackEntry.arguments
                        ?.getString("todoId")
                        ?.toIntOrNull()

                    if (todoId != null) {
                        TodoDetailScreen(
                            todoId = todoId,
                            onBackClick = { navController.popBackStack() }
                        )
                    } else {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
