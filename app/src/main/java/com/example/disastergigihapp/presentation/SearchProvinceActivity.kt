package com.example.disastergigihapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.disastergigihapp.databinding.ActivitySearchProvinceBinding

class SearchProvinceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchProvinceBinding
    private var admin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProvinceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextSearch.isSelected = true

        binding.buttonSearchBack.setOnClickListener {
            
            finish()
        }

        binding.buttonSearchClear.setOnClickListener {
            binding.editTextSearch.setText("")
            admin = null
        }

    }
}