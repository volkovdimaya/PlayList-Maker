package com.practicum.playlistmaker.creator

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
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
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.repository.RepositorySearchHistory
import com.practicum.playlistmaker.domain.repository.ThemeRepository
import com.practicum.playlistmaker.presentation.controller.TrackSearchController
import com.practicum.playlistmaker.presentation.controller.TrackSearchHistoryController
import com.practicum.playlistmaker.ui.search.TrackAdapter


object Creator {

    private lateinit var appContext: Context
    fun initialize(context: Context) {

        appContext = context.applicationContext
    }

    private fun geTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(appContext), TrackResponseMapper)
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

    fun provideTrackSearchController(
        activity: Activity,
        trackSearchHistoryController: TrackSearchHistoryController
    ): TrackSearchController {
        return TrackSearchController(activity, trackSearchHistoryController)
    }

    fun provideTrackSearchHistoryController(
        activity: Activity,
        interactorSearchHistory: InteractorSearchHistory
    ): TrackSearchHistoryController {
        return TrackSearchHistoryController(activity, interactorSearchHistory)
    }

}