package com.example.mytaskmanager.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mytaskmanager.ui.fragments.TasksListFragment

class TaskTabsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TasksListFragment().apply { showCompleted = false }
            1 -> TasksListFragment().apply { showCompleted = true }
            2 -> TasksListFragment().apply { showCompleted = false }
            3 -> TasksListFragment().apply { showCompleted = true }
            else -> Fragment()

        }
    }


}