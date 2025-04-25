package com.example.mytaskmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mytaskmanager.databinding.ActivityMainBinding
import com.example.mytaskmanager.ui.fragments.AddTaskFragment
import com.example.mytaskmanager.ui.fragments.SplashFragment
import com.example.mytaskmanager.ui.fragments.TaskTabsFragment
import com.example.mytaskmanager.ui.fragments.TasksListFragment
import com.example.mytaskmanager.utils.AppConstants

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragmentName = intent.getStringExtra(getString(R.string.name))
        val fragment: Fragment = when (fragmentName) {
            AppConstants.FragmentsTypes.addTaskFragment -> AddTaskFragment()
            AppConstants.FragmentsTypes.tasksListFragment -> TasksListFragment()
            AppConstants.FragmentsTypes.taskTabsFragment -> TaskTabsFragment()
            else -> SplashFragment()
        }
        loadFragments(fragment)
    }

    private fun loadFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}