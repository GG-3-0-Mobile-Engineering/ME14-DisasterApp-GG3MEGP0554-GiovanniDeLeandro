package com.example.disastergigihapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.disastergigihapp.data.local.AppPreferences
import com.example.disastergigihapp.databinding.ActivityDarkModeBinding

class DarkModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDarkModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDarkModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSettingsBack.setOnClickListener { finish() }

        binding.switchDarkMode.isChecked = AppPreferences.getDarkModePreference(this)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            AppPreferences.setDarkModePreference(this, isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate() // Restart the activity to apply the new theme
        }
    }
}