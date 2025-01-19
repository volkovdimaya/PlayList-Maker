package com.practicum.playlistmaker.ui.welcomescreen


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.MedialibraryActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.search.activity.SearchActivity
import com.practicum.playlistmaker.ui.setting.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btn_search)

        btnSearch.setOnClickListener {
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }

        val btnMedia = findViewById<Button>(R.id.btn_media_library)

        btnMedia.setOnClickListener {
            val displayMedialibrary = Intent(this, MedialibraryActivity::class.java)
            startActivity(displayMedialibrary)
        }

        val btnSetting = findViewById<Button>(R.id.btn_setting)

        btnSetting.setOnClickListener {
            val displaySettings = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettings)
        }

    }
}