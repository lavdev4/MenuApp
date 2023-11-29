package com.example.menuapp.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.menuapp.R
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
        if (!checkForInternetConnection()) showToast(getString(R.string.no_interntet_toast))
    }

    private fun checkForInternetConnection(): Boolean {
        val connectivityManager = applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}