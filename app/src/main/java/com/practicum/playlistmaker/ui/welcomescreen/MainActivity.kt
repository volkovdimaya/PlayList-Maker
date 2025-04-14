package com.practicum.playlistmaker.ui.welcomescreen


import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomnavigation)
        bottomNavigationView.setupWithNavController(navController)

        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomnavigation_layout)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addNewPlayListFragment -> {
                    bottomNavigationLayout.visibility = View.GONE
                }

                R.id.audioPlayerFragment -> {
                    bottomNavigationLayout.visibility = View.GONE
                }

                else -> {
                    bottomNavigationLayout.visibility = View.VISIBLE
                }
            }
        }

    }
}