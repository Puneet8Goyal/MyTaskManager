package com.example.mytaskmanager.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.mytaskmanager.R
import com.example.mytaskmanager.databinding.BottomSheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FilterBottomSheet(
    private val context: Context,
    private val calendar: Calendar = Calendar.getInstance(),
    private val dateFormat: SimpleDateFormat = SimpleDateFormat(
        context.getString(R.string.yyyy_mm_dd),
        Locale.getDefault()
    ),

    private val onFiltersApplied: (
        priority: String?, sortOrder: String, sortBy: String, fromDate: String?, toDate: String?
    ) -> Unit
) {
    private var selectedPriority: String? = null
    private var selectedSortOrder: String = context.getString(R.string.asc)
    private var selectedSortBy: String = context.getString(R.string.due_date)
    private var selectedFromDate: String? = null
    private var selectedToDate: String? = null
    private val binding by lazy { BottomSheetFilterBinding.inflate(LayoutInflater.from(context)) }

    private val dialog by lazy {
        BottomSheetDialog(context).also {
            it.setContentView(binding.root)
            initializeUI(it)

        }
    }

    fun open() {
        dialog.show()

    }

    private fun close() {
        dialog.dismiss()

    }

    private fun initializeUI(dialog: BottomSheetDialog) {
        setValues()


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
            // Reset stored filter state
            selectedPriority = null
            selectedSortOrder = context.getString(R.string.asc)
            selectedSortBy = context.getString(R.string.due_date)
            selectedFromDate = null
            selectedToDate = null
            setValues()
            onFiltersApplied(
                null,
                context.getString(R.string.asc),
                context.getString(R.string.due_date),
                null,
                null
            )
            dialog.dismiss()
        }

        binding.btnApplyFilters.setOnClickListener {
            selectedPriority = when {
                binding.rbHigh.isChecked -> context.getString(R.string.high)
                binding.rbMedium.isChecked -> context.getString(R.string.medium)
                binding.rbLow.isChecked -> context.getString(R.string.low)
                else -> null
            }
            selectedSortOrder =
                if (binding.rbAsc.isChecked) context.getString(R.string.asc) else context.getString(
                    R.string.desc
                )
            selectedSortBy =
                if (binding.rbDue.isChecked) context.getString(R.string.due_date) else context.getString(
                    R.string.created_date
                )

            onFiltersApplied(
                selectedPriority,
                selectedSortOrder,
                selectedSortBy,
                selectedFromDate,
                selectedToDate
            )
            close()
        }
    }

    private fun setValues() {

        when (selectedPriority) {
            context.getString(R.string.high) -> binding.rbHigh.isChecked = true
            context.getString(R.string.medium) -> binding.rbMedium.isChecked = true
            context.getString(R.string.low) -> binding.rbLow.isChecked = true
            else -> binding.rgPriority.clearCheck()
        }

        when (selectedSortOrder) {
            context.getString(R.string.asc) -> binding.rbAsc.isChecked = true
            context.getString(R.string.desc) -> binding.rbDesc.isChecked = true
        }

        when (selectedSortBy) {
            context.getString(R.string.due_date) -> binding.rbDue.isChecked = true
            context.getString(R.string.created_date) -> binding.rbCreated.isChecked = true
        }

        binding.tvFromDate.text = selectedFromDate ?: context.getString(R.string.select_from_date)
        binding.tvToDate.text = selectedToDate ?: context.getString(R.string.select_to_date)
    }
}