package com.practicum.playlistmaker


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class SettingsActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        val toolbar: Toolbar = findViewById(R.id.toolbar_setting)

        toolbar.setNavigationOnClickListener {
            finish()
        }

    }

}