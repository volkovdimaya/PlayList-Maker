package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.ui.setting.App
import com.practicum.playlistmaker.domain.repository.ThemeRepository

class ThemeInteractor(private val repository : ThemeRepository, private val app: App) {

    fun isDarkTheme(): Boolean = repository.isDarkTheme()

    fun switchTheme(isDark: Boolean) {
        repository.saveTheme(isDark)
        app.switchTheme(isDark)
    }
}