package com.example.real_project_mobile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.real_project_mobile.data.AppDatabase
import com.example.real_project_mobile.data.Todo
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.items


@Composable
fun ViewTodosScreen(
    onTodoClick: (Todo) -> Unit = {},
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // Get DAO & Flow only once
    val todoDao = remember { AppDatabase.getDatabase(context).todoDao() }
    val incompleteTodosFlow: Flow<List<Todo>> = remember { todoDao.getIncompleteTodos() }

    val todos by incompleteTodosFlow.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Header
        Text(
            text = "ToDo App",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // List of todos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(todos) { todo ->
                TodoListItem(
                    todo = todo,
                    onViewDetails = { onTodoClick(todo) }
                )
            }
        }
    }
}

@Composable
private fun TodoListItem(
    todo: Todo,
    onViewDetails: () -> Unit
) {
    val dateText = remember(todo.dueDate) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(Date(todo.dueDate))
    }



            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = todo.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Deadline: $dateText",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = todo.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onViewDetails,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("View Details")
            }
        }

