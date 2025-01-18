package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.repository.ThemeRepository
import com.practicum.playlistmaker.domain.setting.ThemeSwitcher

class ThemeInteractor(
    private val repository: ThemeRepository,
    private val themeSwitcher: ThemeSwitcher,
) {
    fun isDarkTheme(): Boolean = repository.isDarkTheme()

    fun isFirstStart() : Boolean {
       return repository.isFirstStart()
    }

    fun switchTheme(isDark: Boolean) {
        repository.saveTheme(isDark)
        themeSwitcher.switchTheme(isDark)
    }
}