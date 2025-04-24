package com.example.mytaskmanager.ui.list

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mytaskmanager.R
import com.example.mytaskmanager.databinding.ItemTaskBinding
import com.example.mytaskmanager.model.Task

class TaskAdapter(
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit,
    private val onToggleStatusClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: List<Task> = emptyList()

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        with(holder.binding) {
            if (position == taskList.size - 1) {
                bottomView.isVisible = true
            }
            tvTitle.text = task.title
            tvDescription.text = task.description
            tvPriority.text = "Priority: ${task.priority}"
            tvDueDate.text = "Due: ${task.dueDate}"
            chipStatus.text = if (task.isCompleted) "✅ Completed" else "🕒 Pending"
            chipStatus.chipBackgroundColor = ColorStateList.valueOf(
                if (task.isCompleted) "#C8E6C9".toColorInt() else "#FFE0B2".toColorInt()
            )
            if (task.isCompleted && task.completedOnTime.isNotBlank()) {
                tvCompletedStatus.text = "${task.completedOnTime}"
                tvCompletedStatus.visibility = View.VISIBLE
            } else {
                tvCompletedStatus.visibility = View.GONE
            }
            btnToggleComplete.text = if (task.isCompleted) "Mark as Pending" else "Mark as Done"
        }
        holder.binding.btnToggleComplete.setOnClickListener {
            onToggleStatusClick(task)
        }
        holder.binding.btnMenu.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.binding.btnMenu)
            popup.menuInflater.inflate(R.menu.task_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        onEdit(task)
                        true
                    }

                    R.id.menu_delete -> {
                        onDelete(task)
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }
        holder.binding.tvCreatedDate.text = "Created: ${task.createdAt}"
    }

    override fun getItemCount(): Int = taskList.size

    fun setTasks(tasks: List<Task>) {
        this.taskList = tasks
        notifyDataSetChanged()
    }
}