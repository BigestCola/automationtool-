package com.automation.automationtool.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.automation.automationtool.model.Task
import com.automation.automationtool.model.TaskStatus
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(onTaskAdded: (Task) -> Unit, onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var title by remember { mutableStateOf("") }
                var description by remember { mutableStateOf("") }
                var executeTime by remember { mutableStateOf(System.currentTimeMillis()) }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                // TODO: 添加执行时间选择器

                Button(
                    onClick = {
                        val newTask = Task(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            createTime = System.currentTimeMillis(),
                            executeTime = executeTime,
                            status = TaskStatus.PENDING
                        )
                        onTaskAdded(newTask)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Add Task")
                }
            }
        }
    )
}