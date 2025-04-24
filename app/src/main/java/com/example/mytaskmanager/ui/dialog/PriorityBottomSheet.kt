package com.example.mytaskmanager.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import com.example.mytaskmanager.databinding.BottomSheetPriorityBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PriorityBottomSheet(
    private val context: Context,
    private val onPrioritySelected: (String) -> Unit
) {
    fun open(selectedPriority: String = "") {
        val binding = BottomSheetPriorityBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(binding.root)

        fun highlight(selected: String) {
            val all = listOf(binding.tvHigh, binding.tvMedium, binding.tvLow)
            all.forEach { it.setBackgroundResource(android.R.color.darker_gray) }

            when (selected) {
                "High" -> binding.tvHigh.setBackgroundResource(android.R.color.darker_gray)
                "Medium" -> binding.tvMedium.setBackgroundResource(android.R.color.darker_gray)
                "Low" -> binding.tvLow.setBackgroundResource(android.R.color.darker_gray)
            }
        }
        highlight(selectedPriority)

        binding.tvHigh.setOnClickListener {
            onPrioritySelected("High")
            dialog.dismiss()
        }

        binding.tvMedium.setOnClickListener {
            onPrioritySelected("Medium")
            dialog.dismiss()
        }

        binding.tvLow.setOnClickListener {
            onPrioritySelected("Low")
            dialog.dismiss()
        }

        dialog.show()
    }
}