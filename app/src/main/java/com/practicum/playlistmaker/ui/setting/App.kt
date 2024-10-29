package com.practicum.playlistmaker.ui.setting

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.repository.MODE_THEME
import com.practicum.playlistmaker.data.repository.PLAYLIST_MAKER


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val interactorTheme = Creator().provideInteractorTheme(this)
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