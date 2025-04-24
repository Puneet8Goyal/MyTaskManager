package com.example.mytaskmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mytaskmanager.databinding.ActivityMainBinding
import com.example.mytaskmanager.ui.tabs.TaskTabsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TaskTabsFragment())
                .commit()
        }
    }
}