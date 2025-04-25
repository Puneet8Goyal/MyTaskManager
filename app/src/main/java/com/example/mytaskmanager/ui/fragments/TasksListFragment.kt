package com.example.mytaskmanager.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytaskmanager.R
import com.example.mytaskmanager.data.database.TaskDatabase
import com.example.mytaskmanager.databinding.FragmentTasksListBinding
import com.example.mytaskmanager.model.Task
import com.example.mytaskmanager.repository.TaskRepository
import com.example.mytaskmanager.ui.adapters.TaskAdapter
import com.example.mytaskmanager.ui.dialog.ChangeTaskStatusDialog
import com.example.mytaskmanager.ui.dialog.FilterBottomSheet
import com.example.mytaskmanager.utils.AppConstants
import com.example.mytaskmanager.utils.launchNextPage
import com.example.mytaskmanager.viewmodel.TaskViewModel
import com.example.mytaskmanager.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class TasksListFragment : Fragment() {

    private val binding by lazy { FragmentTasksListBinding.inflate(layoutInflater) }
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
     var showCompleted: Boolean = false
    private var allFilteredTasks: List<Task> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private val sheet by lazy {
        FilterBottomSheet(
            requireContext(),

            ) { priority, order, sortBy, fromDate, toDate ->
            applyFilterFromBottomSheet(priority, order, sortBy, fromDate, toDate)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        showCompleted = arguments?.getBoolean(getString(R.string.isCompleted)) ?: false

        val sortOptions: List<String> = listOf(
            getString(R.string.none),
            getString(R.string.priority),
            getString(R.string.due_date),
            getString(R.string.created_date)
        )
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            sortOptions
        )
        binding.spinnerSort.adapter = spinnerAdapter
        binding.recyclerView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_fall_down)

        adapter = TaskAdapter(
            onEdit = { selectedTask ->
                requireContext().launchNextPage(
                    AppConstants.FragmentsTypes.addTaskFragment,
                    init = { putExtra(getString(R.string.task), selectedTask) })

            },
            onDelete = { taskToDelete ->
                viewModel.deleteTask(taskToDelete)
            },
            onToggleStatusClick = { task ->
                if (!task.isCompleted) {
                    ChangeTaskStatusDialog(requireContext(), task) { updatedTask ->
                        viewModel.updateTask(updatedTask)
                    }.show()
                } else {
                    val updated = task.copy(isCompleted = false, completedOnTime = "")
                    viewModel.updateTask(updated)
                }
            }
        )

        binding.recyclerView.adapter = adapter

        binding.etSearch.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            val sortBy = binding.spinnerSort.selectedItem.toString()
            applyFiltersAndSort(query, sortBy)
        }

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sortBy = parent.getItemAtPosition(position).toString()
                val query = binding.etSearch.text.toString()
                applyFiltersAndSort(query, sortBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Set up filter button
        binding.btnFilter.setOnClickListener {
            sheet.open()
        }

        binding.fabAddTask.setOnClickListener {
            requireContext().launchNextPage(AppConstants.FragmentsTypes.addTaskFragment)
        }

        viewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            val filtered = tasks.filter { it.isCompleted == showCompleted }
            allFilteredTasks = filtered
            applyFiltersAndSort(
                binding.etSearch.text.toString(),
                binding.spinnerSort.selectedItem.toString(),
            )
            binding.recyclerView.scheduleLayoutAnimation()
        }
    }

    // Function to apply filters from the bottom sheet
    fun applyFilterFromBottomSheet(
        priority: String?,
        order: String?,
        sortBy: String?,
        fromDate: String?,
        toDate: String?
    ) {
        val sdf = SimpleDateFormat(getString(R.string.yyyy_mm_dd), Locale.getDefault())
        val filtered = allFilteredTasks
            .filter { task ->
                val taskDate = try {
                    sdf.parse(task.dueDate)
                } catch (e: Exception) {
                    null
                }

                val passPriority = priority == null || task.priority.equals(
                    priority,
                    ignoreCase = true
                )
                val passDateRange =
                    if (fromDate != null && toDate != null && taskDate != null) {
                        val from = sdf.parse(fromDate)
                        val to = sdf.parse(toDate)
                        taskDate in from..to
                    } else true

                passPriority && passDateRange
            }.let {
                when (sortBy) {
                    getString(R.string.due_date) -> if (order == getString(R.string.asc)) it.sortedBy { t -> t.dueDate } else it.sortedByDescending { t -> t.dueDate }
                    getString(R.string.created_date) -> if (order == getString(R.string.asc)) it.sortedBy { t -> t.createdAt } else it.sortedByDescending { t -> t.createdAt }
                    else -> it
                }
            }
        adapter.setTasks(filtered)
    }

    private fun applyFiltersAndSort(query: String = "", sortBy: String = getString(R.string.none)) {
        var filtered = allFilteredTasks

        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.title?.contains(query, true) == true ||
                        it.description?.contains(query, true) == true
            }
        }

        filtered = when (sortBy) {
            getString(R.string.priority) -> filtered.sortedBy {
                when (it.priority?.lowercase()) {
                    getString(R.string.high) -> 1
                    getString(R.string.medium) -> 2
                    getString(R.string.low) -> 3
                    else -> 4
                }
            }

            getString(R.string.due_date) -> filtered.sortedBy { it.dueDate }
            getString(R.string.created_date) -> filtered.sortedBy { it.createdAt }
            else -> filtered
        }

        adapter.setTasks(filtered)
    }

    companion object {
        fun newInstance(showCompleted: Boolean): TasksListFragment {
            val fragment = TasksListFragment()
            val args = Bundle()
            args.putBoolean("isCompleted", showCompleted)
            fragment.arguments = args
            return fragment
        }
    }
}