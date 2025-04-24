package com.example.mytaskmanager.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.mytaskmanager.databinding.BottomSheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FilterBottomSheet(
    private val context: Context,
    private val calendar: Calendar = Calendar.getInstance(),
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
    private var selectedFromDate: String? = null,
    private var selectedToDate: String? = null,
    private val onFiltersApplied: (
        priority: String?, sortOrder: String, sortBy: String, fromDate: String?, toDate: String?
    ) -> Unit,

    ) {

    fun open(currentPriority: String? = null) {
        val binding = BottomSheetFilterBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(binding.root)

        when (currentPriority) {
            "High" -> binding.rbHigh.isChecked = true
            "Medium" -> binding.rbMedium.isChecked = true
            "Low" -> binding.rbLow.isChecked = true
        }
        binding.rbAsc.isChecked = true
        binding.rbDue.isChecked = true

        binding.tvFromDate.setOnClickListener {
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedFromDate = dateFormat.format(calendar.time)
                    binding.tvFromDate.text = selectedFromDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvToDate.setOnClickListener {
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedToDate = dateFormat.format(calendar.time)
                    binding.tvToDate.text = selectedToDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnClearFilters.setOnClickListener {
            onFiltersApplied(null, "ASC", "None", null, null)
            dialog.dismiss()
        }


        binding.btnApplyFilters.setOnClickListener {
            val selectedPriority = when {
                binding.rbHigh.isChecked -> "High"
                binding.rbMedium.isChecked -> "Medium"
                binding.rbLow.isChecked -> "Low"
                else -> null
            }
            val sortOrder = if (binding.rbAsc.isChecked) "ASC" else "DESC"
            val sortBy = if (binding.rbDue.isChecked) "Due Date" else "Created Date"
            onFiltersApplied(selectedPriority, sortOrder, sortBy, selectedFromDate, selectedToDate)
            dialog.dismiss()
        }
        dialog.show()
    }

}