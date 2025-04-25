package com.example.mytaskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = null,
    val description: String? = null,
    val priority: String? = null,
    val dueDate: String? = null,
    val isCompleted: Boolean = false,
    val completedOnTime: String = "",
    val createdAt: String? = null,
) : Serializable