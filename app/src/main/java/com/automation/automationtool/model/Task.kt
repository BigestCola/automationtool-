package com.automation.automationtool.model

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val createTime: Long,
    val executeTime: Long,
    val status: TaskStatus
)

enum class TaskStatus {
    PENDING, RUNNING, COMPLETED, FAILED
}