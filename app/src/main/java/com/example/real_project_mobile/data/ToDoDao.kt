package com.example.real_project_mobile.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY dueDate ASC")
    fun getAllTodos(): Flow<List<Todo>>


    @Query("SELECT * FROM todos WHERE isComplete = 0 ORDER BY dueDate ASC")
    fun getIncompleteTodos(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("DELETE FROM todos WHERE isComplete = 1")
    suspend fun deleteCompletedTodos()
}
