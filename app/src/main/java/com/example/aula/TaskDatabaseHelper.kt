package com.example.aula
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_TASKS = "tasks"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableSql)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun insertTask(title: String, description: String): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
        }

        val result = db.insert(TABLE_TASKS, null, values)

        db.close()

        return result
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()

        val db = readableDatabase

        val cursor = db.query(
            TABLE_TASKS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC"
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_ID)
            )

            val title = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_TITLE)
            )

            val description = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
            )

            val task = Task(
                id = id,
                title = title,
                description = description
            )

            tasks.add(task)
        }

        cursor.close()
        db.close()

        return tasks
    }

    fun updateTask(task: Task): Int {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
        }

        val result = db.update(
            TABLE_TASKS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(task.id.toString())
        )

        db.close()

        return result
    }

    fun deleteTask(taskId: Int): Int {
        val db = writableDatabase

        val result = db.delete(
            TABLE_TASKS,
            "$COLUMN_ID = ?",
            arrayOf(taskId.toString())
        )

        db.close()

        return result
    }
}