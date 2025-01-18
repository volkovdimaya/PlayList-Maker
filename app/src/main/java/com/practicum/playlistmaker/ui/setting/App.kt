package com.practicum.playlistmaker.ui.setting

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.repository.MODE_THEME
import com.practicum.playlistmaker.data.repository.PLAYLIST_MAKER


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initialize(applicationContext)
        val interactorTheme = Creator.providerThemeInteractor()
        switchTheme(interactorTheme.isDarkTheme())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}