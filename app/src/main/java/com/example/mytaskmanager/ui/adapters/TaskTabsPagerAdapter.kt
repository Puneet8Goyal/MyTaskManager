package com.example.mytaskmanager.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mytaskmanager.ui.fragments.TasksListFragment

class TaskTabsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TasksListFragment().apply { showCompleted = false }
            1 -> TasksListFragment().apply { showCompleted = true }
            else -> Fragment()

        }
    }


}