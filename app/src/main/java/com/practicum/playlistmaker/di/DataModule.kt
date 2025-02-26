package com.practicum.playlistmaker.di


import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.audioplayer.ManagerAudioPlayerImpl
import com.practicum.playlistmaker.data.audioplayer.PlayerTimerImpl
import com.practicum.playlistmaker.data.mapper.TrackDtoResponseMapper
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper
import com.practicum.playlistmaker.data.repository.PLAYLIST_MAKER
import com.practicum.playlistmaker.data.search.network.NetworkClient
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.search.network.SearchTrackApi
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorimpl
import com.practicum.playlistmaker.data.sharing.impl.ResourceProviderImpl
import com.practicum.playlistmaker.domain.player.ManagerAudioPlayer
import com.practicum.playlistmaker.domain.player.PlayerTimer
import com.practicum.playlistmaker.domain.setting.ThemeSwitcher
import com.practicum.playlistmaker.domain.sharing.ResourceProvider
import com.practicum.playlistmaker.ui.setting.App
import com.practicum.playlistmaker.ui.setting.AppThemeSwitcher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single { TrackDtoResponseMapper }
    single { TrackResponseMapper }

    single<ManagerAudioPlayer> {
        ManagerAudioPlayerImpl(get())
    }
    single {
        MediaPlayer()
    }
    single<PlayerTimer> {
        PlayerTimerImpl()
    }

    single<SearchTrackApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchTrackApi::class.java)
    }


    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }



    single<ExternalNavigator> {
        ExternalNavigatorimpl(androidContext())
    }
    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get() )
    }


    single {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER , AppCompatActivity.MODE_PRIVATE)
    }

    single<ThemeSwitcher> { AppThemeSwitcher(androidContext() as App) }



}