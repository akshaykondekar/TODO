package com.example.todo.ui

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todo.R
import com.example.todo.model.Priority
import com.example.todo.model.Todo

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(todoList: List<Todo>) {
        emptyDatabase.value = todoList.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?, view: View?,
            position: Int, id: Long
        ) {
            when (position) {
                0 -> { (parent?.getChildAt(0) as? TextView)?.setTextColor(ContextCompat.getColor(application, R.color.colorRed)) }
                1 -> { (parent?.getChildAt(0) as? TextView)?.setTextColor(ContextCompat.getColor(application, R.color.colorYellow)) }
                2 -> { (parent?.getChildAt(0) as? TextView)?.setTextColor(ContextCompat.getColor(application, R.color.colorGreen)) }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    fun validateData(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> {
                Priority.HIGH
            }
            "Medium Priority" -> {
                Priority.MEDIUM
            }
            "Low Priority" -> {
                Priority.LOW
            }
            else -> Priority.LOW
        }
    }

}