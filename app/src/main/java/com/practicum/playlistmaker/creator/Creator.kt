package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.ui.setting.App
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.mapper.TrackDtoResponseMapper
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.PLAYLIST_MAKER
import com.practicum.playlistmaker.data.repository.RepositorySearchHistoryImpl
import com.practicum.playlistmaker.data.repository.ThemeRepositoryImpl

import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.repository.RepositorySearchHistory
import com.practicum.playlistmaker.domain.repository.ThemeRepository


object Creator {

    private lateinit var appContext: Context
    fun initialize(context: Context) {

        appContext = context.applicationContext
    }

    private fun geTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(), TrackResponseMapper)
    }

    fun provideTracksInteractor(): TrackInteractorApi {
        return TracksInteractorImpl(geTracksRepository())
    }

    private fun getSharedPreferences(): SharedPreferences {
        return appContext.getSharedPreferences(PLAYLIST_MAKER, AppCompatActivity.MODE_PRIVATE)
    }

    fun provideInteractorSearchHistory(): RepositorySearchHistory {
        return RepositorySearchHistoryImpl(getSharedPreferences(), TrackDtoResponseMapper)
    }

    private fun getRepositoryTheme(): ThemeRepository {
        return ThemeRepositoryImpl(getSharedPreferences())
    }

    fun provideInteractorTheme(): ThemeInteractor {

        return ThemeInteractor(getRepositoryTheme(), appContext)
    }

}