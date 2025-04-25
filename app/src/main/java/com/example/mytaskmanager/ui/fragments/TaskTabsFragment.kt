package com.example.mytaskmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mytaskmanager.R
import com.example.mytaskmanager.databinding.FragmentTaskTabsBinding
import com.example.mytaskmanager.ui.adapters.TaskTabsPagerAdapter
import com.example.mytaskmanager.ui.dialog.FilterBottomSheet
import com.google.android.material.tabs.TabLayoutMediator

class TaskTabsFragment : Fragment() {
    private val binding by lazy { FragmentTaskTabsBinding.inflate(layoutInflater) }
    private lateinit var adapter: TaskTabsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                0 -> getString(R.string.pending)
                1 -> getString(R.string.completed)
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