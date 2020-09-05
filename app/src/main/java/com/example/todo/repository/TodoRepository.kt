package com.example.todo.repository

import androidx.lifecycle.LiveData
import com.example.todo.db.TodoDAO
import com.example.todo.model.Todo

class TodoRepository(private val todoDAO: TodoDAO) {

    val getAllTodo: LiveData<List<Todo>> = todoDAO.getAllTodo()
    val sortByHighPriority : LiveData<List<Todo>> = todoDAO.sortByHighPriority()
    val sortByLowPriority : LiveData<List<Todo>> = todoDAO.sortByLowPriority()

    suspend fun insertTodo(todo: Todo) = todoDAO.insertTodo(todo)

    suspend fun updateTodo(todo: Todo) = todoDAO.updateTodo(todo)

    suspend fun deleteTodo(todo: Todo) = todoDAO.deleteTodo(todo)

    suspend fun deleteAllTodo() = todoDAO.deleteAllTodo()

    fun searchTodo(searchQuery : String) : LiveData<List<Todo>> {
        return todoDAO.searchTodo(searchQuery)
    }
}