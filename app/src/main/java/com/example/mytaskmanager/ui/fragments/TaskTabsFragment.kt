package com.example.mytaskmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytaskmanager.R
import com.example.mytaskmanager.data.database.TaskDatabase
import com.example.mytaskmanager.databinding.FragmentTaskTabsBinding
import com.example.mytaskmanager.repository.TaskRepository
import com.example.mytaskmanager.ui.adapters.TaskTabsPagerAdapter
import com.example.mytaskmanager.ui.dialog.FilterBottomSheet
import com.example.mytaskmanager.viewmodel.TaskViewModel
import com.example.mytaskmanager.viewmodel.TaskViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class TaskTabsFragment : Fragment() {
    private val binding by lazy { FragmentTaskTabsBinding.inflate(layoutInflater) }
    private lateinit var adapter: TaskTabsPagerAdapter
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[TaskViewModel::class.java]

        // Set up ViewPager and adapter
        adapter = TaskTabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        // Set up TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.pending)
                1 -> getString(R.string.completed)
                2 -> getString(R.string.pending)
                3 -> getString(R.string.completed)
                else -> ""
            }
        }.attach()

        // Set up toolbar menu
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> {
                    // Show filter bottom sheet with ViewModel
                    FilterBottomSheet(requireContext(), viewModel).open()
                    true
                }

                else -> false
            }
        }
    }
}