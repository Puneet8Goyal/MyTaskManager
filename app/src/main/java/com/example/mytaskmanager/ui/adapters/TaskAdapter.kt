package com.example.mytaskmanager.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.toColorInt
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        with(holder.binding) {

            val context = holder.binding.root.context

            tvTitle.text = task.title
            tvDescription.text = task.description
            tvPriority.text = context.getString(R.string.priority, task.priority)
            tvDueDate.text = context.getString(R.string.due, task.dueDate)
            chipStatus.text =
                if (task.isCompleted) context.getString(R.string.completed) else context.getString(R.string.pending)
            chipStatus.chipBackgroundColor = ColorStateList.valueOf(
                if (task.isCompleted) "#C8E6C9".toColorInt() else "#FFE0B2".toColorInt()
            )
            if (task.isCompleted && task.completedOnTime.isNotBlank()) {
                tvCompletedStatus.text = task.completedOnTime
                tvCompletedStatus.visibility = View.VISIBLE
            } else {
                tvCompletedStatus.visibility = View.GONE
            }
            btnToggleComplete.text =
                if (task.isCompleted) context.getString(R.string.mark_as_pending) else context.getString(
                    R.string.mark_as_done
                )
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
        holder.binding.tvCreatedDate.text =
            holder.binding.root.context.getString(R.string.created, task.createdAt)
    }

    override fun getItemCount(): Int = taskList.size

    fun setTasks(tasks: List<Task>) {
        this.taskList = tasks
        notifyDataSetChanged()
    }
}