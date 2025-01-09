package com.practicum.playlistmaker.domain.repository

interface ThemeRepository {
    fun saveTheme(isDark: Boolean)
    fun isDarkTheme(): Boolean
}