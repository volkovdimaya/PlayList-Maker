package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.audioplayer.ManagerAudioPlayerImpl
import com.practicum.playlistmaker.data.search.TracksRepositoryImpl
import com.practicum.playlistmaker.data.mapper.TrackDtoResponseMapper
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.PLAYLIST_MAKER
import com.practicum.playlistmaker.data.repository.RepositorySearchHistoryImpl
import com.practicum.playlistmaker.data.repository.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorimpl
import com.practicum.playlistmaker.data.sharing.impl.ResourceProviderImpl

import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.player.ManagerAudioPlayer
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.player.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.player.interactor.TrackPlayerImpl
import com.practicum.playlistmaker.domain.search.RepositorySearchHistory
import com.practicum.playlistmaker.domain.repository.ThemeRepository
import com.practicum.playlistmaker.domain.sharing.ResourceProvider
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl


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


    fun providerThemeInteractor(): ThemeInteractor {
        return ThemeInteractor(getRepositoryTheme(), appContext)
    }

    fun providerSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(providerExternalNavigator(), providerResourceProvider())
    }
    private fun providerExternalNavigator() : ExternalNavigator
    {
        return ExternalNavigatorimpl(appContext)
    }
    private fun providerResourceProvider() : ResourceProvider{
        return ResourceProviderImpl(appContext)
    }

   private fun providerManagerAudioPlayer(): ManagerAudioPlayer {
        return ManagerAudioPlayerImpl(MediaPlayer())
    }
    fun providerAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractor(providerManagerAudioPlayer())
    }
    fun providerTrackPlayer(previewUrl : String): TrackPlayer {
        return TrackPlayerImpl(previewUrl, providerAudioPlayerInteractor())
    }




}