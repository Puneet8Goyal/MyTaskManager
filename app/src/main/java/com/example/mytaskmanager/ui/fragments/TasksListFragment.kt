package com.example.mytaskmanager.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.mytaskmanager.repository.TaskRepository
import com.example.mytaskmanager.ui.adapters.TaskAdapter
import com.example.mytaskmanager.ui.dialog.ChangeTaskStatusDialog
import com.example.mytaskmanager.ui.dialog.FilterBottomSheet
import com.example.mytaskmanager.utils.AppConstants
import com.example.mytaskmanager.utils.launchNextPage
import com.example.mytaskmanager.viewmodel.TaskViewModel
import com.example.mytaskmanager.viewmodel.TaskViewModelFactory

class TasksListFragment : Fragment() {

    private val binding by lazy { FragmentTasksListBinding.inflate(layoutInflater) }
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    var showCompleted: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("onCreateView", ": ", )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("onViewCreated", ": ", )
        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)

        // Get ViewModel from parent activity to share state between fragments
        viewModel = ViewModelProvider(requireActivity(), factory)[TaskViewModel::class.java]

        setupSortSpinner()
        setupRecyclerView()
        setupSearchListener()
        setupFilterButton()
        setupAddTaskButton()
        observeViewModel()
    }

    private fun setupSortSpinner() {
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

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val option = parent.getItemAtPosition(position).toString()
                val sortBy = when (option) {
                    getString(R.string.priority) -> "priority"
                    getString(R.string.due_date) -> "due_date"
                    getString(R.string.created_date) -> "created_date"
                    else -> "none"
                }
                viewModel.setFilterParameters(sortBy = sortBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRecyclerView() {
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
    }

    private fun setupSearchListener() {
        binding.etSearch.addTextChangedListener { editable ->
            viewModel.setSearchQuery(editable.toString().trim())
        }
    }

    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            FilterBottomSheet(requireContext(), viewModel).open()
        }
    }

    private fun setupAddTaskButton() {
        binding.fabAddTask.setOnClickListener {
            requireContext().launchNextPage(AppConstants.FragmentsTypes.addTaskFragment)
        }
    }

    private fun observeViewModel() {
        // Observe filtered tasks from ViewModel
        viewModel.filteredTasks.observe(viewLifecycleOwner) { tasks ->
            val filteredTasks = tasks.filter { it.isCompleted == showCompleted }
            adapter.setTasks(filteredTasks) //Updates the adapter with the new task list.
            binding.recyclerView.scheduleLayoutAnimation()
        }
    }
}