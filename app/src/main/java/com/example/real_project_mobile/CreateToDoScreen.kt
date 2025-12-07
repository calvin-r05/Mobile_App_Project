package com.example.real_project_mobile

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.real_project_mobile.data.Todo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CreateTodoScreen(
    onSave: (Todo) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDateString by remember { mutableStateOf("") }




    fun parseDateToMillis(date: String): Long? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false
            val parsedDate = sdf.parse(date)
            parsedDate?.time
        } catch (e: Exception) {
            null
        }
    }


    fun openDatePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val formatted = "%02d/%02d/%04d".format(day, month + 1, year)
                dueDateString = formatted
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Create New Todo",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // NAME
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // DESCRIPTION
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))



        Spacer(modifier = Modifier.height(12.dp))

        // DATE PICKER BUTTON
        Button(
            onClick = { openDatePicker() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (dueDateString.isEmpty()) "Select Due Date" else dueDateString)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SAVE BUTTON
        Button(
            onClick = {
                val millis = parseDateToMillis(dueDateString)

                if (name.isBlank() || description.isBlank() || millis == null) {
                    Toast.makeText(context, "Please fill all fields and pick a valid date.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val newTodo = Todo(
                    name = name,
                    description = description,
                    dueDate = millis
                )

                onSave(newTodo)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Todo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // CANCEL BUTTON
        OutlinedButton(
            onClick = { onCancel() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
