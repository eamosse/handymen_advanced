package com.vama.android.handymen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.vama.android.handymen.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Clé de préférence pour le mode
    private val PREF_MODE_KEY = "isDatabaseMode"
    private lateinit var prefs: SharedPreferences

    // Mode courant : true = Database, false = Memory
    private var isDatabaseMode: Boolean = false

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        isDatabaseMode = prefs.getBoolean(PREF_MODE_KEY, false)

        if (isDatabaseMode) {
            activateDatabaseMode()
        } else {
            activateMemoryMode()
        }

        val navView: BottomNavigationView = binding.navView

        // Get NavController from NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup the bottom navigation with the NavController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_favorites
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun activateDatabaseMode() {

    }

    private fun activateMemoryMode() {

    }
}