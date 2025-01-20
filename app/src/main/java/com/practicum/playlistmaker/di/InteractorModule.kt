package com.practicum.playlistmaker.di


import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.player.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }
    single<TrackInteractorApi> {
        TracksInteractorImpl(get())
    }
    single<TrackPlayer> { (previewUrl : String) ->
        AudioPlayerInteractor(previewUrl, get(), get())
    }
    single {
        ThemeInteractor(get(), get() )
    }
    single {
        InteractorSearchHistory(get() )
    }

}