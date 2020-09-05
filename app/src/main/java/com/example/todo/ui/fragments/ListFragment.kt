package com.example.todo.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.FragmentListBinding
import com.example.todo.helper.SwipeToDelete
import com.example.todo.helper.hideKeyboard
import com.example.todo.model.Todo
import com.example.todo.ui.SharedViewModel
import com.example.todo.ui.TodoViewModel
import com.example.todo.ui.adapter.TodoAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val todoViewModel: TodoViewModel by viewModels()
    private val todoAdapter: TodoAdapter by lazy { TodoAdapter() }
    private val sharedViewModel: SharedViewModel by viewModels()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        // to exit if there no more fragments
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            })


        setHasOptionsMenu(true)
        hideKeyboard(requireActivity())
        setupRecyclerview()

        todoViewModel.getAllData.observe(viewLifecycleOwner, { list ->
            sharedViewModel.checkIfDatabaseEmpty(list)
            todoAdapter.updateList(list)
        })

        return binding.root
    }

    private fun setupRecyclerview() {
        binding.rvToDo.apply {
            adapter = todoAdapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            swipeToDelete(this)
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = todoAdapter.todoList[viewHolder.adapterPosition]
                todoViewModel.deleteTodo(itemToDelete)
                todoAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(
                    requireContext(),
                    "ToDo deleted successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                restoredDeletedTodo(viewHolder.itemView, itemToDelete)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoredDeletedTodo(view: View, deletedTodo: Todo) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedTodo.title}'",
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Undo") {
            todoViewModel.insertTodo(deletedTodo)
        }
        snackBar.show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        todoViewModel.searchTodo(searchQuery)
            .observe(viewLifecycleOwner, { searchedList ->

                searchedList?.let {
                    todoAdapter.updateList(it)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionDeleteAll -> confirmDeleteAll()
            R.id.actionPriorityHigh -> todoViewModel.sortByHighPriority.observe(this,
                {
                    todoAdapter.updateList(it)
                })
            R.id.actionPriorityLow -> todoViewModel.sortByLowPriority.observe(this,
                {
                    todoAdapter.updateList(it)
                })
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteAllTodo()
            Toast.makeText(
                requireContext(),
                "All ToDo deleted successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete All ToDo")
        builder.setMessage("Do you really want to delete all ToDo?")
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}