package com.example.aula

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aula.databinding.ActivityMainBinding
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.collections.emptyList


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: TaskDatabaseHelper
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = TaskDatabaseHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainContainer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(16, systemBars.top, 16, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        loadTasks()

        binding.buttonAddTask.setOnClickListener{
            addExampleTask()
        }

        showTaskCount()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            tasks = emptyList(),
            onTaskClick = { task ->
                showEditTaskDialog(task)
            },
            onTaskLongClick = { task ->
                showDeleteConfirmationDialog(task)
            }
        )

        binding.recyclerTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerTasks.adapter = taskAdapter
    }

    private fun loadTasks() {
        val tasks = databaseHelper.getAllTasks()
        taskAdapter.updateTasks(tasks)
    }

    fun addExampleTask() {
        val result = databaseHelper.insertTask(
            title = "Estudar Kotlin",
            description = "Praticar SQLiteOpenHelper"
        )

        if (result != -1L) {
            Toast.makeText(
                this,
                "Tarefa cadastrada com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            loadTasks()
        } else {
            Toast.makeText(
                this,
                "Erro ao cadastrar tarefa",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showEditTaskDialog(task: Task) {
        val inputTitle = EditText(this)
        inputTitle.hint = "Título"
        inputTitle.setText(task.title)

        val inputDescription = EditText(this)
        inputDescription.hint = "Descrição"
        inputDescription.setText(task.description)

        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        container.setPadding(50, 20, 50, 0)

        container.addView(inputTitle)
        container.addView(inputDescription)

        AlertDialog.Builder(this)
            .setTitle("Editar tarefa")
            .setView(container)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Salvar") { _, _ ->
                val updatedTitle = inputTitle.text.toString()
                val updatedDescription = inputDescription.text.toString()

                val updatedTask = Task(
                    id = task.id,
                    title = updatedTitle,
                    description = updatedDescription
                )

                updateTask(updatedTask)
            }
            .show()
    }

    private fun updateTask(task: Task) {
        val result = databaseHelper.updateTask(task)

        if (result > 0) {
            Toast.makeText(
                this,
                "Tarefa atualizada com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            loadTasks()
        } else {
            Toast.makeText(
                this,
                "Erro ao atualizar tarefa",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showTaskCount() {
        val tasks = databaseHelper.getAllTasks()

        Toast.makeText(
            this,
            "Você tem ${tasks.size} tarefa(s) cadastrada(s)",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showDeleteConfirmationDialog(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Excluir tarefa")
            .setMessage("Deseja excluir a tarefa \"${task.title}\"?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Excluir") { _, _ ->
                deleteTask(task)
            }
            .show()
    }

    private fun deleteTask(task: Task) {
        val result = databaseHelper.deleteTask(task.id)

        if (result > 0) {
            Toast.makeText(
                this,
                "Tarefa excluída com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            loadTasks()
        } else {
            Toast.makeText(
                this,
                "Erro ao excluir tarefa",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}