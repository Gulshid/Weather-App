package com.gulshid.weatherapp.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yourname.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity — all screens are Fragments.
 * @AndroidEntryPoint allows Hilt to inject into this Activity.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}