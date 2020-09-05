package com.example.todo.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.databinding.FragmentUpdateBinding
import com.example.todo.model.Todo
import com.example.todo.ui.SharedViewModel
import com.example.todo.ui.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val todoViewModel: TodoViewModel by viewModels()
    private lateinit var todo: Todo
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        setHasOptionsMenu(true)

        todo = args.currentTodo

        binding.spUpdatePriority.onItemSelectedListener = sharedViewModel.listener
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionUpdate -> updateTodo()
            R.id.actionDelete -> deleteTodo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTodo() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteTodo(args.currentTodo)
            Toast.makeText(
                requireContext(),
                "ToDo deleted successfully!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete '${args.currentTodo.title}'?")
        builder.setMessage("Do you really want to delete this ToDo?")
        builder.create().show()
    }

    private fun updateTodo() {
        val title = etUpdateTitle.text.toString()
        val description = etUpdateDescription.text.toString()
        val priority = spUpdatePriority.selectedItem.toString()

        val validation = sharedViewModel.validateData(title, description)

        if (validation) {
            todo.title = title
            todo.description = description
            todo.priority = sharedViewModel.parsePriority(priority)

            todoViewModel.updateTodo(todo)
            Snackbar.make(clMainUpdate, "TODO updated successfully", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(this.activity, "Please fill the details", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}