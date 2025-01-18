package com.practicum.playlistmaker.ui.setting

import com.practicum.playlistmaker.domain.setting.ThemeSwitcher

class AppThemeSwitcher(private val app: App) : ThemeSwitcher {
    override fun switchTheme(isDark: Boolean) {
        app.switchTheme(isDark)
    }
}