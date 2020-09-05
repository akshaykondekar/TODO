package com.example.todo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.db.TodoDatabase
import com.example.todo.model.Todo
import com.example.todo.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = TodoDatabase.getDatabase(application).todoDAO()
    private val repository: TodoRepository

    val getAllData: LiveData<List<Todo>>
    val sortByHighPriority : LiveData<List<Todo>>
    val sortByLowPriority : LiveData<List<Todo>>

    init {
        repository = TodoRepository(todoDao)
        getAllData = repository.getAllTodo
        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
    }

    fun insertTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }

    fun deleteAllTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTodo()
        }
    }

    fun searchTodo(searchQuery : String) : LiveData<List<Todo>>{
        return repository.searchTodo(searchQuery)
    }
}