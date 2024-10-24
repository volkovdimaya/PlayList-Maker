package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.PLAYLIST_MAKER
import com.practicum.playlistmaker.data.repository.RepositorySearchHistoryImpl

import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.repository.RepositorySearchHistory


class Creator {

    private fun geTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(geTracksRepository())
    }

    fun getSharedPreferences(context: Context) : SharedPreferences
    {
        return context.getSharedPreferences(PLAYLIST_MAKER, AppCompatActivity.MODE_PRIVATE)
    }
    fun provideInteractorSearchHistory(context: Context) : RepositorySearchHistory
    {
        return RepositorySearchHistoryImpl(getSharedPreferences(context))
    }
}