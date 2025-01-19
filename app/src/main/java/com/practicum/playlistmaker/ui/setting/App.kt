package com.practicum.playlistmaker.ui.setting

import android.app.Application

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import android.content.res.Configuration


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initialize(applicationContext)
        val interactorTheme = Creator.providerThemeInteractor()
        if (!interactorTheme.isFirstStart()) {
            val isSystemDark =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            interactorTheme.switchTheme(isSystemDark)
            switchTheme(isSystemDark)
        } else {
            switchTheme(interactorTheme.isDarkTheme())
        }


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