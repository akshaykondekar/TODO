package com.example.todo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.CvTodoListItemBinding
import com.example.todo.helper.TodoDiffUtil
import com.example.todo.model.Todo

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    var todoList = emptyList<Todo>()

    class ViewHolder(private val binding: CvTodoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.todo = todo
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CvTodoListItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTodo = todoList[position]
        holder.bind(currentTodo)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun updateList(updatedTodoList: List<Todo>) {
        val todoDiffUtil = TodoDiffUtil(todoList, updatedTodoList)
        val todoDiffResult = DiffUtil.calculateDiff(todoDiffUtil)
        this.todoList = updatedTodoList
        todoDiffResult.dispatchUpdatesTo(this)
    }
}