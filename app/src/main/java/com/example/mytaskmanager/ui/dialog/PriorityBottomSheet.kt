package com.example.mytaskmanager.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import com.example.mytaskmanager.R
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
                context.getString(R.string.high) -> binding.tvHigh.setBackgroundResource(android.R.color.darker_gray)
                context.getString(R.string.medium) -> binding.tvMedium.setBackgroundResource(android.R.color.darker_gray)
                context.getString(R.string.low) -> binding.tvLow.setBackgroundResource(android.R.color.darker_gray)
            }
        }
        highlight(selectedPriority)

        binding.tvHigh.setOnClickListener {
            onPrioritySelected(context.getString(R.string.high))
            dialog.dismiss()
        }

        binding.tvMedium.setOnClickListener {
            onPrioritySelected(context.getString(R.string.medium))
            dialog.dismiss()
        }

        binding.tvLow.setOnClickListener {
            onPrioritySelected(context.getString(R.string.low))
            dialog.dismiss()
        }

        dialog.show()
    }
}