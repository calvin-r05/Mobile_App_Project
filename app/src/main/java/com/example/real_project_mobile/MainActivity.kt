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
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope

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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFB388FF),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Image Placeholder")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onViewTodosClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Todos")
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
                        onTodoClick = { /* later if you want details */ },
                        onBackClick = { navController.popBackStack() }
                    )
                }



                // CREATE TODO
                composable("create_todo") {
                    CreateTodoScreen(
                        onSave = { todo ->
                            scope.launch {
                                todoDao.insertTodo(todo)
                            }
                            navController.popBackStack() // go back after saving
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
