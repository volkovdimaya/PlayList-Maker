

package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.repository.MODE_THEME
import com.practicum.playlistmaker.data.repository.PLAYLIST_MAKER


class App : Application() {

    private var darkTheme = false

    override fun onCreate() {

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(MODE_THEME, false)
        switchTheme(darkTheme)

        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}