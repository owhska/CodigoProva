package com.example.aula

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aula.databinding.ItemTaskBinding

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskLongClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            task: Task,
            onTaskClick: (Task) -> Unit,
            onTaskLongClick: (Task) -> Unit
        ) {
            binding.textTaskTitle.text = task.title
            binding.textTaskDescription.text = task.description

            binding.root.setOnClickListener {
                onTaskClick(task)
            }

            binding.root.setOnLongClickListener {
                onTaskLongClick(task)
                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        val task = tasks[position]
        holder.bind(
            task = task,
            onTaskClick = onTaskClick,
            onTaskLongClick = onTaskLongClick
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
