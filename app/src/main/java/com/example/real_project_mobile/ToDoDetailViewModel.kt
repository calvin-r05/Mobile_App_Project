package com.example.real_project_mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.real_project_mobile.data.Todo
import com.example.real_project_mobile.data.TodoDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoDetailViewModel(
    private val dao: TodoDao,
    private val todoId: Int
) : ViewModel() {


    val todo: StateFlow<Todo?> =
        dao.getTodoById(todoId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun markComplete() {
        val current = todo.value ?: return
        viewModelScope.launch {
            dao.updateTodo(current.copy(isComplete = true))
        }
    }

    fun updateTodo(name: String, description: String, dueDateMillis: Long) {
        val current = todo.value ?: return
        viewModelScope.launch {
            dao.updateTodo(
                current.copy(
                    name = name,
                    description = description,
                    dueDate = dueDateMillis
                )
            )
        }
    }

    fun deleteTodo() {
        val current = todo.value ?: return
        viewModelScope.launch {
            dao.deleteTodo(current)
        }
    }

    companion object {
        fun provideFactory(
            dao: TodoDao,

            todoId: Int
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TodoDetailViewModel::class.java)) {
                        return TodoDetailViewModel(dao, todoId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
