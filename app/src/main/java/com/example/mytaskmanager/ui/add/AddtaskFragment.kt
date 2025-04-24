package com.example.mytaskmanager.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytaskmanager.R
import com.example.mytaskmanager.data.database.TaskDatabase
import com.example.mytaskmanager.databinding.FragmentAddTaskBinding
import com.example.mytaskmanager.model.Task
import com.example.mytaskmanager.repository.TaskRepository
import com.example.mytaskmanager.ui.dialog.PriorityBottomSheet
import com.example.mytaskmanager.viewmodel.TaskViewModel
import com.example.mytaskmanager.viewmodel.TaskViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: TaskViewModel
    private var selectedTask: Task? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]


        (requireActivity() as androidx.appcompat.app.AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val priorityClickListener = View.OnClickListener {
            PriorityBottomSheet(requireContext()) { selected ->
                binding.etPriority.setText(selected)
            }.open(binding.etPriority.text.toString())
        }

        binding.etPriority.setOnClickListener(priorityClickListener)
        binding.tilPriority.setEndIconOnClickListener(priorityClickListener)

        // Set up date picker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Due Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(MaterialDatePicker.todayInUtcMilliseconds()) // Disable past dates
                    .build()
            )
            .build()

        // Show date picker when clicking the due date field
        binding.etDueDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        // Show date picker when clicking the end icon
        binding.tilDueDate.setEndIconOnClickListener {
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        // Handle date selection
        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            val formattedDate = dateFormat.format(selectedDate)
            binding.etDueDate.setText(formattedDate)
        }

        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            val priority = binding.etPriority.text.toString()
            val dueDate = binding.etDueDate.text.toString()


            if (title.isEmpty() || priority.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(requireContext(), "Title & Priority required", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val task = Task(
                id = selectedTask?.id ?: 0,
                title = title,
                description = description,
                priority = priority,
                dueDate = dueDate,
                isCompleted = selectedTask?.isCompleted ?: false,
                createdAt = dateFormat.format(Date())
            )

            if (selectedTask != null) {
                viewModel.updateTask(task)
                Toast.makeText(requireContext(), "Task updated!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertTask(task)
                Toast.makeText(requireContext(), "Task added!", Toast.LENGTH_SHORT).show()
            }
            parentFragmentManager.popBackStack()

        }

        selectedTask = arguments?.getSerializable("task") as? Task

        selectedTask?.let { task ->
            binding.etTitle.setText(task.title)
            binding.etDescription.setText(task.description)
            binding.etPriority.setText(task.priority)
            binding.etDueDate.setText(task.dueDate)
            binding.btnAdd.text = "Update Task"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Restore the FAB when this fragment is removed
        (activity?.findViewById<View>(R.id.fabAddTask))?.visibility = View.VISIBLE
    }

}