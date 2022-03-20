package com.rafsan.jetpackcompose_state_management.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.rafsan.jetpackcompose_state_management.ui.StateCodelabTheme

class TodoActivity : AppCompatActivity() {

    val todoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateCodelabTheme {
                Surface {
                    TodoActivityScreen(todoViewModel = todoViewModel)
                }
            }
        }
    }

    @Composable
    private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
        val items: List<TodoItem> by todoViewModel.todoItems.observeAsState(listOf())
        TodoScreen(
            items = items,
            onAddItem = { todoViewModel.addItem(it) },
            onRemoveItem = { todoViewModel.removeItem(it) }
        )
    }
}