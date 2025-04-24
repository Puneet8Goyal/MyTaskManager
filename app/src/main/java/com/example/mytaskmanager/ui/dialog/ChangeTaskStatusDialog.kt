package com.example.mytaskmanager.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import com.example.mytaskmanager.databinding.DialogCompletionStatusBinding
import com.example.mytaskmanager.model.Task

class ChangeTaskStatusDialog(
    private val context: Context,
    private val task: Task,
    private val onSubmit: (Task) -> Unit
) {

    fun show() {
        val binding = DialogCompletionStatusBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        // Restore current status if present
        when (task.completedOnTime) {
            "Completed Early" -> binding.rbEarly.isChecked = true
            "Completed On Time" -> binding.rbOnTime.isChecked = true
            "Completed Late" -> binding.rbLate.isChecked = true
        }

        // Set up confirm button
        binding.btnConfirm.setOnClickListener {
            val selectedId = binding.radioGroupStatus.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(context, "Please select a completion status", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedButton = binding.radioGroupStatus.findViewById<RadioButton>(selectedId)
            val completionStatus = selectedButton.text.toString()
                .replace("✅ ", "")
                .replace("⏰ ", "")
                .replace("❌ ", "")

            val updatedTask = task.copy(
                isCompleted = true,
                completedOnTime = completionStatus
            )

            onSubmit(updatedTask)
            dialog.dismiss()
        }

        dialog.show()
    }
}