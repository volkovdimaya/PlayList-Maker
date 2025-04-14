package com.practicum.playlistmaker.di


import com.practicum.playlistmaker.domain.add_playlist.interactor.InteractorSavePicImpl
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.add_playlist.interactor.DbInteractorPlaylistImpl
import com.practicum.playlistmaker.domain.add_playlist.use_case.ValidateDescription
import com.practicum.playlistmaker.domain.add_playlist.use_case.ValidateImage
import com.practicum.playlistmaker.domain.add_playlist.use_case.ValidateTitle
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.db.InteractorFavorite
import com.practicum.playlistmaker.domain.db.UseCaseGetFavoritesTracks
import com.practicum.playlistmaker.domain.db.use_case.InteractorFavoriteImpl
import com.practicum.playlistmaker.domain.db.use_case.UseCaseGetFavoritesTracksImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.search.interactor.InteractorSearchHistoryImpl
import com.practicum.playlistmaker.domain.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.player.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.search.InteractorSearchHistory
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.ui.library.add_playlist.api.InteractorSavePic
import org.koin.dsl.module

val interactorModule = module {

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }
    single<TrackInteractorApi> {
        TracksInteractorImpl(get())
    }
    factory<TrackPlayer> { (previewUrl: String) ->
        AudioPlayerInteractor(previewUrl, get())
    }
    single {
        ThemeInteractor(get(), get())
    }
    single<InteractorSearchHistory> {
        InteractorSearchHistoryImpl(get())
    }

    single<UseCaseGetFavoritesTracks> { UseCaseGetFavoritesTracksImpl(get()) }
    single<InteractorFavorite> { InteractorFavoriteImpl(get()) }

    single<InteractorSavePic> {
        InteractorSavePicImpl(get())
    }

    single<DbInteractorPlaylist> { DbInteractorPlaylistImpl(get(), get()) }
    single { ValidateDescription() }
    single { ValidateImage() }
    single { ValidateTitle() }

}