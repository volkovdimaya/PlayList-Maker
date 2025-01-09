package com.practicum.playlistmaker.domain.interactor


import android.content.Context
import com.practicum.playlistmaker.ui.setting.App
import com.practicum.playlistmaker.domain.repository.ThemeRepository

class ThemeInteractor(private val repository : ThemeRepository, private val app: Context) {
    fun isDarkTheme(): Boolean = repository.isDarkTheme()
    fun switchTheme(isDark: Boolean) {
        repository.saveTheme(isDark)
        (app as App).switchTheme(isDark)
    }
}