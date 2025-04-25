package com.example.mytaskmanager.utils

interface AppConstants {

    companion object {
        const val DB_NAME = "task_db"
    }

    object FragmentsTypes {
        const val addTaskFragment = "AddTaskFragment"
        const val tasksListFragment = "TasksListFragment"
        const val taskTabsFragment = "TaskTabsFragment"
    }


}