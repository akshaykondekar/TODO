package com.example.todo.ui

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.todo.R
import com.example.todo.model.Priority
import com.example.todo.model.Todo
import com.example.todo.ui.fragments.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {

    companion object {

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(fabAdd: FloatingActionButton, navigate: Boolean) {
            fabAdd.setOnClickListener {
                if (navigate) {
                    fabAdd.findNavController().navigate(
                        R.id.action_listFragment_to_addFragment
                    )
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = VISIBLE
                false -> view.visibility = GONE
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) {
            when (priority) {
                Priority.HIGH -> {
                    view.setSelection(0)
                }
                Priority.MEDIUM -> {
                    view.setSelection(1)
                }
                Priority.LOW -> {
                    view.setSelection(2)
                }
            }
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriority(cardView: CardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.colorRed))
                }
                Priority.MEDIUM -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.colorYellow))
                }
                Priority.LOW -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.colorGreen))
                }
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentTodo: Todo) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentTodo)
                view.findNavController().navigate(action)
            }
        }
    }
}