package com.example.mytaskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val priority: String,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val completedOnTime: String = "",
    val createdAt: String,
) : Serializable