package com.automation.automationtool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.automation.automationtool.ui.theme.AutomationToolTheme
import com.example.automationtool.ui.MainScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import com.automation.automationtool.model.Task
import com.automation.automationtool.model.TaskStatus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sampleTasks = listOf(
                Task("1", "Task 1", "Description 1", System.currentTimeMillis(), System.currentTimeMillis() + 3600000, TaskStatus.PENDING),
                Task("2", "Task 2", "Description 2", System.currentTimeMillis(), System.currentTimeMillis() + 7200000, TaskStatus.RUNNING),
                Task("3", "Task 3", "Description 3", System.currentTimeMillis(), System.currentTimeMillis() - 3600000, TaskStatus.COMPLETED)
            )
            MainScreen(tasks = sampleTasks)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutomationToolTheme {
        Greeting("Android")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val tasks = remember { mutableStateListOf<Task>() }
    // ...
}