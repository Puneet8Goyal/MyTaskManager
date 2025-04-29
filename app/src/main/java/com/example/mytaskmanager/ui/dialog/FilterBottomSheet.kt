package com.example.mytaskmanager.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.mytaskmanager.R
import com.example.mytaskmanager.databinding.BottomSheetFilterBinding
import com.example.mytaskmanager.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FilterBottomSheet(
    private val context: Context,
    private val viewModel: TaskViewModel,
    private val calendar: Calendar = Calendar.getInstance(),
    private val dateFormat: SimpleDateFormat = SimpleDateFormat(
        context.getString(R.string.yyyy_mm_dd),
        Locale.getDefault()
    )
) {
    private val binding by lazy { BottomSheetFilterBinding.inflate(LayoutInflater.from(context)) }
    private val dialog by lazy {
        BottomSheetDialog(context).also {
            it.setContentView(binding.root)
            initializeUI()
        }
    }

    // Values currently selected in UI (will be applied on Apply button click)
    private var selectedPriority: String? = null
    private var selectedSortOrder: String = context.getString(R.string.asc)
    private var selectedSortBy: String = context.getString(R.string.due_date)
    private var selectedFromDate: String? = null
    private var selectedToDate: String? = null

    fun open() {
        // Initialize values from ViewModel
        viewModel.selectedPriority.value?.let { selectedPriority = it }
        viewModel.sortOrder.value?.let { selectedSortOrder = it }
        viewModel.sortBy.value?.let { selectedSortBy = it }
        viewModel.fromDate.value?.let { selectedFromDate = it }
        viewModel.toDate.value?.let { selectedToDate = it }

        setValues()
        dialog.show()
    }

    private fun close() {
        dialog.dismiss()
    }

    private fun initializeUI() {
        setupDatePickers()
        setupButtons()
    }

    private fun setupDatePickers() {
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
    }

    private fun setupButtons() {
        binding.btnClearFilters.setOnClickListener {
            // Reset stored filter state in ViewModel
            viewModel.clearFilters(context)

            // Reset local state
            selectedPriority = null
            selectedSortOrder = context.getString(R.string.asc)
            selectedSortBy = context.getString(R.string.due_date)
            selectedFromDate = null
            selectedToDate = null

            setValues()
            close()
        }

        binding.btnApplyFilters.setOnClickListener {
            // Update local variables based on UI selections
            selectedPriority = when {
                binding.rbHigh.isChecked -> context.getString(R.string.high)
                binding.rbMedium.isChecked -> context.getString(R.string.medium)
                binding.rbLow.isChecked -> context.getString(R.string.low)
                else -> null
            }

            selectedSortOrder = if (binding.rbAsc.isChecked)
                context.getString(R.string.asc)
            else
                context.getString(R.string.desc)

            selectedSortBy = if (binding.rbDue.isChecked)
                context.getString(R.string.due_date)
            else
                context.getString(R.string.created_date)

            // Apply filters to ViewModel
            viewModel.setFilterParameters(
                priority = selectedPriority,
                sortOrder = selectedSortOrder,
                sortBy = selectedSortBy,
                fromDate = selectedFromDate,
                toDate = selectedToDate
            )

            close()
        }
    }

    private fun setValues() {
        // Set priority radio buttons
        when (selectedPriority) {
            context.getString(R.string.high) -> binding.rbHigh.isChecked = true
            context.getString(R.string.medium) -> binding.rbMedium.isChecked = true
            context.getString(R.string.low) -> binding.rbLow.isChecked = true
            else -> binding.rgPriority.clearCheck()
        }

        // Set sort order radio buttons
        when (selectedSortOrder) {
            context.getString(R.string.asc) -> binding.rbAsc.isChecked = true
            context.getString(R.string.desc) -> binding.rbDesc.isChecked = true
        }

        // Set sort by radio buttons
        when (selectedSortBy) {
            context.getString(R.string.due_date) -> binding.rbDue.isChecked = true
            context.getString(R.string.created_date) -> binding.rbCreated.isChecked = true
        }

        // Set date fields
        binding.tvFromDate.text = selectedFromDate ?: context.getString(R.string.select_from_date)
        binding.tvToDate.text = selectedToDate ?: context.getString(R.string.select_to_date)
    }
}