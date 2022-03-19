package com.rafsan.jetpackcompose_state_management.todo

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.rafsan.jetpackcompose_state_management.R
import java.util.*

data class TodoItem(
    val task: String,
    val icon: TodoIcon = TodoIcon.Default,
    // since the user may generate identical tasks, give them each a unique ID
    val id: UUID = UUID.randomUUID()
)

enum class TodoIcon(val imageVector: ImageVector, @StringRes val contentDescription: Int) {
    Square(Icons.Default.CropSquare, R.string.cd_expand),
    Done(Icons.Default.Done, R.string.cd_done),
    Event(Icons.Default.Event, R.string.cd_event),
    Privacy(Icons.Default.PrivacyTip, R.string.cd_privacy),
    Trash(Icons.Default.RestoreFromTrash, R.string.cd_restore);

    companion object {
        val Default = Square
    }
}