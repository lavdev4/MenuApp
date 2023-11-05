package com.example.menuapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.menuapp.databinding.ActivityMainBinding
import com.example.menuapp.di.MainActivitySubcomponent

class MainActivity : AppCompatActivity() {

    lateinit var mainActivitySubcomponent: MainActivitySubcomponent
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivitySubcomponent = (application as MenuApplication).applicationComponent
            .activitySubcomponent()
            .build()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}