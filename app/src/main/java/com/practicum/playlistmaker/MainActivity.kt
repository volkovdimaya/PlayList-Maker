package com.practicum.playlistmaker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSerch = findViewById<Button>(R.id.btn_serch)

        val btnSearchClickListener : View.OnClickListener  = object : View.OnClickListener
        {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на поиск!", Toast.LENGTH_SHORT).show()
            }

        }

        btnSerch.setOnClickListener(btnSearchClickListener)

        val btnMedia = findViewById<Button>(R.id.btn_media)

        btnMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на медиатека!", Toast.LENGTH_SHORT).show()
        }

        val btnSetting = findViewById<Button>(R.id.btn_setting)

        btnSetting.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на настройки!", Toast.LENGTH_SHORT).show()
        }

    }
}