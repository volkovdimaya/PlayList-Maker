package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.repository.ThemeRepository

class ThemeRepositoryImpl(private val sharedPrefs: SharedPreferences): ThemeRepository {

    override fun isDarkTheme(): Boolean = sharedPrefs.getBoolean(MODE_THEME, false)

    override fun saveTheme(isDark: Boolean) {
        sharedPrefs.edit().putBoolean(MODE_THEME, isDark).apply()
    }
}