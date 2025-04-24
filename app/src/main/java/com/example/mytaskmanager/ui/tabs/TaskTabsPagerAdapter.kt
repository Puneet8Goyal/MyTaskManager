package com.example.mytaskmanager.ui.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mytaskmanager.ui.list.TasksListFragment

class TaskTabsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TasksListFragment.newInstance(false)
            1 -> TasksListFragment.newInstance(true)
            else -> Fragment()

        }
    }


}