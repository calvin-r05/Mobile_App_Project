package com.example.real_project_mobile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.real_project_mobile.data.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodoDetailScreen(
    todoId: Int,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).todoDao() }


    val detailViewModel: TodoDetailViewModel = viewModel(
        factory = TodoDetailViewModel.provideFactory(dao, todoId)
    )

    val todo by detailViewModel.todo.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDateText by remember { mutableStateOf("") }

    LaunchedEffect(todo) {
        todo?.let {
            name = it.name
            description = it.description
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dueDateText = sdf.format(Date(it.dueDate))
        }
    }

    fun parseDateToMillis(date: String): Long? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false
            val parsed = sdf.parse(date)
            parsed?.time
        } catch (e: Exception) {
            null
        }
    }

    if (todo == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading todo…")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "ToDo Details",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Deadline: $dueDateText",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = dueDateText,
            onValueChange = { dueDateText = it },
            label = { Text("Deadline (dd/MM/yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        //  Mark as Complete
        Button(
            onClick = {
                detailViewModel.markComplete()
                onBackClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mark as Complete")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Update
        Button(
            onClick = {
                val millis = parseDateToMillis(dueDateText)
                if (millis != null) {
                    detailViewModel.updateTodo(name, description, millis)
                    onBackClick()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update ToDo")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Delete
        Button(
            onClick = {
                detailViewModel.deleteTodo()
                onBackClick()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Delete ToDo")
        }
    }
}
