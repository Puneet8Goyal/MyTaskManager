package com.example.mytaskmanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mytaskmanager.data.dao.TaskDao
import com.example.mytaskmanager.model.Task
import com.example.mytaskmanager.utils.AppConstants


@Database(
    entities = [Task::class],
    version = 3,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TaskDatabase::class.java,
                    AppConstants.DB_NAME
                ).addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create a new table with updated schema (non-nullable fields)
        db.execSQL("""
            CREATE TABLE tasks_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT,
                priority TEXT NOT NULL,
                dueDate TEXT NOT NULL,
                isCompleted INTEGER NOT NULL DEFAULT 0,
                completedOnTime TEXT NOT NULL DEFAULT '',
                createdAt TEXT NOT NULL
            )
        """)
        // Copy data from old table, providing default values for null fields
        db.execSQL("""
            INSERT INTO tasks_new (id, title, description, priority, dueDate, isCompleted, completedOnTime, createdAt)
            SELECT 
                id, 
                COALESCE(title, 'Untitled'), 
                description, 
                COALESCE(priority, 'Medium'), 
                COALESCE(dueDate, '1970-01-01'), 
                isCompleted, 
                COALESCE(completedOnTime, ''), 
                COALESCE(createdAt, '1970-01-01')
            FROM tasks
        """)
        // Drop old table and rename new table
        db.execSQL("DROP TABLE tasks")
        db.execSQL("ALTER TABLE tasks_new RENAME TO tasks")
    }
}