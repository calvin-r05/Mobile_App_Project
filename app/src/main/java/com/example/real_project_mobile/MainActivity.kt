// MainActivity.kt
package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoApp()
        }
    }
}

@Composable
fun ToDoApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ToDoHomeScreen(
                onViewTodosClick = {
                    // TODO: navigate to your Todos screen
                }
            )
        }
    }
}

@Composable
fun ToDoHomeScreen(
    onViewTodosClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "ToDo App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xFFB388FF), // light purple-ish border
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Image Placeholder")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // View Todos Button
        Button(
            onClick = onViewTodosClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "View Todos")
        }
    }
}
