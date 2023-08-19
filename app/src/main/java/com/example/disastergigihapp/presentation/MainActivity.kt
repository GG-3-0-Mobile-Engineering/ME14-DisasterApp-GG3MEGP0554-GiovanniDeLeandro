package com.example.disastergigihapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disastergigihapp.R
import com.example.disastergigihapp.data.local.AppPreferences
import com.example.disastergigihapp.presentation.adapter.DisasterAdapter
import com.example.disastergigihapp.databinding.ActivityMainBinding
import com.example.disastergigihapp.presentation.viewmodel.MainViewModel
import com.example.disastergigihapp.util.ApiState
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainBinding: ActivityMainBinding
    private var timeperiod: Int = 604800
//    private var admin: String = ""
//    private var disaster: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppPreferences.getDarkModePreference(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val mapView = mainBinding.mapView
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val rvDisaster = findViewById<RecyclerView>(R.id.rvDisaster)
        rvDisaster.setHasFixedSize(true)
        rvDisaster.layoutManager = LinearLayoutManager(this)

        mainBinding.buttonSettings.setOnClickListener {
            val intent = Intent(this, DarkModeActivity::class.java)
            startActivity(intent)
        }

        mainBinding.editSearch.setOnClickListener {
            val intent = Intent(this, SearchProvinceActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.loadMap(mapView)

        mainViewModel.getRecent(timeperiod)

        lifecycleScope.launchWhenStarted {
            mainViewModel.postStateFlows.collect {
                when (it) {
                    is ApiState.Loading -> {
                        rvDisaster.isVisible = false
                        progressBar.isVisible = true
                    }

                    is ApiState.Failure -> {
                        rvDisaster.isVisible = false
                        progressBar.isVisible = false
                        when (it.msg) {
                            is SocketTimeoutException, is UnknownHostException -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Internet Anda mati",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is HttpException -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Server Error" + it.msg.code(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            else -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Generic Error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    is ApiState.Success -> {
                        rvDisaster.isVisible = true
                        progressBar.isVisible = false

                        val disasters = it.data.result?.objects?.output?.geometries!!
                        disasters.forEachIndexed { index, _ ->
                            mainViewModel.addAnnotationToMap(
                                disasters[index].coordinates!![0],
                                disasters[index].coordinates!![1],
                                this@MainActivity,
                                mapView
                            )
                        }
                        val adapter = DisasterAdapter(disasters)
                        rvDisaster.adapter = adapter
                    }

                    is ApiState.Empty -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Tidak ada bencana pada waktu dan tempat yang dipilih",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }
}