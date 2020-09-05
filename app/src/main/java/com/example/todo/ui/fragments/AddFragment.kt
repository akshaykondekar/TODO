package com.example.todo.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.model.Todo
import com.example.todo.ui.SharedViewModel
import com.example.todo.ui.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val todoViewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        setHasOptionsMenu(true)
        view.spPriority.onItemSelectedListener = sharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionAdd) {
            insertToDo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertToDo() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val priority = spPriority.selectedItem.toString()

        val validation = sharedViewModel.validateData(title, description)

        if (validation) {
            val newTodo = Todo(
                title,
                sharedViewModel.parsePriority(priority),
                description
            )

            todoViewModel.insertTodo(newTodo)
            Snackbar.make(clMainAdd, "TODO added successfully", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(this.activity, "Please fill the details", Toast.LENGTH_SHORT).show()
        }
    }
}