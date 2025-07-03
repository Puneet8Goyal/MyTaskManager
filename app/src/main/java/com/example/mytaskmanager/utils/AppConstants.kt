package com.example.taskease.utils

interface AppConstants {

    companion object {
        const val DB_NAME = "task_db"
        const val YYYY_MM_DD = "yyyy_mm_dd"
    }

    object FragmentsTypes {
        const val addTaskFragment = "AddTaskFragment"
        const val tasksListFragment = "TasksListFragment"
        const val taskTabsFragment = "TaskTabsFragment"
    }


}