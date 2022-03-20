package com.rafsan.jetpackcompose_state_management.examples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafsan.jetpackcompose_state_management.databinding.ActivityExampleBinding

/**
 * An example showing unstructured state stored in an Activity.
 */
class ExampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExampleBinding
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // doAfterTextChange is an event that modifies state
        binding.textInput.doAfterTextChanged { text ->
            name = text.toString()
            updateName()
        }
    }

    /**
     * This function updates the screen to show the current state of [name]
     */
    private fun updateName() {
        binding.nameText.text = "Hello, $name"
    }
}

/**
 * A ViewModel extracts _state_ from the UI and defines _events_ that can update it.
 */
class ExampleViewModel : ViewModel() {

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // onNameChanged is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onNameChanged(newName: String) {
        _name.value = newName
    }
}

/**
 * An example showing unidirectional data flow in the View system using a ViewModel.
 */
class ExampleActivityWithViewModel : AppCompatActivity() {
    private val exampleViewModel by viewModels<ExampleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // doAfterTextChange is an event that triggers an event on the ViewModel
        binding.textInput.doAfterTextChanged {
            // onNameChanged is an event on the ViewModel
            exampleViewModel.onNameChanged(it.toString())
        }
        // [exampleViewModel.name] is state that we observe to update the UI
        exampleViewModel.name.observe(this) { name ->
            binding.nameText.text = "Hello, $name"
        }
    }
}

class ExampleActivityCompose : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExampleScreen()
        }
    }
}

@Composable
private fun ExampleScreen(exampleViewModel: ExampleViewModel = viewModel()) {
    // exampleViewModel follows the Lifecycle as the the Activity or Fragment that calls this
    // composable function.

    // name is the _current_ value of [exampleViewModel.name]
    val name: String by exampleViewModel.name.observeAsState("")

    ExampleInput(name = name, onNameChange = { exampleViewModel.onNameChanged(it) })
}

@Composable
private fun ExampleScreenWithInternalState() {
    val (name, setName) = remember { mutableStateOf("") }
    ExampleInput(name = name, onNameChange = setName)
}

/**
 * @param name (state) current text to display
 * @param onNameChange (event) request that text change
 */
@Composable
private fun ExampleInput(
    name: String,
    onNameChange: (String) -> Unit
) {
    Column {
        Text(name)
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}