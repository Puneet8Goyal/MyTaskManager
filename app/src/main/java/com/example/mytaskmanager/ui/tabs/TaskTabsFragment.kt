package com.example.mytaskmanager.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mytaskmanager.R
import com.example.mytaskmanager.databinding.FragmentTaskTabsBinding
import com.example.mytaskmanager.ui.dialog.FilterBottomSheet
import com.example.mytaskmanager.ui.list.TasksListFragment
import com.google.android.material.tabs.TabLayoutMediator

class TaskTabsFragment : Fragment() {
    private lateinit var binding: FragmentTaskTabsBinding
    private lateinit var adapter: TaskTabsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up ViewPager and adapter
        adapter = TaskTabsPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Set up TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pending"
                1 -> "Completed"
                else -> ""
            }
        }.attach()

        // Set up toolbar menu
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> {
                    // Show filter bottom sheet and pass filter parameters to the current TasksListFragment
                    val currentFragment = childFragmentManager.fragments
                        .filterIsInstance<TasksListFragment>()
                        .getOrNull(binding.viewPager.currentItem)
                    currentFragment?.let { tasksListFragment ->
                        FilterBottomSheet(requireContext()) { priority, order, sortBy, fromDate, toDate ->
                            tasksListFragment.applyFilterFromBottomSheet(
                                priority,
                                order,
                                sortBy,
                                fromDate,
                                toDate
                            )
                        }.open()
                    }
                    true
                }

                else -> false
            }
        }
    }
}