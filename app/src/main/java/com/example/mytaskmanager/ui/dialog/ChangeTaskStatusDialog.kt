package com.example.mytaskmanager.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import com.example.mytaskmanager.R
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
        restoreCurrentStatus(binding)

        // Set up confirm button
        setupConfirmButton(binding, dialog)

        dialog.show()
    }

    private fun restoreCurrentStatus(binding: DialogCompletionStatusBinding) {
        when (task.completedOnTime) {
            context.getString(R.string.completed_early) -> binding.rbEarly.isChecked = true
            context.getString(R.string.completed_on_time) -> binding.rbOnTime.isChecked = true
            context.getString(R.string.completed_late) -> binding.rbLate.isChecked = true
        }
    }

    private fun setupConfirmButton(binding: DialogCompletionStatusBinding, dialog: AlertDialog) {
        binding.btnConfirm.setOnClickListener {
            val selectedId = binding.radioGroupStatus.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(
                    context,
                    context.getString(R.string.please_select_a_completion_status),
                    Toast.LENGTH_SHORT
                ).show()
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
    }
}