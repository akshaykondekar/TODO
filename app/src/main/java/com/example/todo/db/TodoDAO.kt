package com.example.todo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.model.Todo

@Dao
interface TodoDAO {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTodo(): LiveData<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTodo()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
    fun searchTodo(searchQuery : String) : LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE '%M' THEN 2 WHEN priority LIKE '%L' THEN 3 END")
    fun sortByHighPriority() : LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE '%M' THEN 2 WHEN priority LIKE '%H' THEN 3 END")
    fun sortByLowPriority() : LiveData<List<Todo>>
}