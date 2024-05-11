package com.example.automationtool.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automation.automationtool.ui.theme.AutomationToolTheme
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.lazy.items
import com.automation.automationtool.model.Task
import com.automation.automationtool.model.TaskStatus
import androidx.compose.runtime.mutableStateOf
import com.automation.automationtool.ui.AddTaskScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(tasks: List<Task>) {
    val tasks = remember { mutableStateListOf<Task>().apply { addAll(initialTasks) } }
    val showAddTaskScreen = remember { mutableStateOf(false) }



    FloatingActionButton(
        onClick = { showAddTaskScreen.value = true }
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add")
    }

    if (showAddTaskScreen.value) {
        AddTaskScreen(
            onTaskAdded = { newTask ->
                tasks.add(newTask)
                showAddTaskScreen.value = false
            },
            onBackPressed = {
                showAddTaskScreen.value = false
            }
        )
    }

    AutomationToolTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("龙归凤田") },
                    actions = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    if (tasks.isEmpty()) {
                        Text("No tasks yet.", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(tasks) { task ->
                                TaskItem(task)
                            }
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${task.status}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Create Time: ${formatTime(task.createTime)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview
@Composable
fun MainScreenPreview() {
    val sampleTasks = listOf(
        Task("1", "任务 1", "描述 1", System.currentTimeMillis(), System.currentTimeMillis() + 3600000, TaskStatus.PENDING),
        Task("2", "任务 2", "捆述 2", System.currentTimeMillis(), System.currentTimeMillis() + 7200000, TaskStatus.RUNNING),
        Task("3", "任务 3", "捆述 3", System.currentTimeMillis(), System.currentTimeMillis() - 3600000, TaskStatus.COMPLETED)
    )
    MainScreen(tasks = sampleTasks)
}