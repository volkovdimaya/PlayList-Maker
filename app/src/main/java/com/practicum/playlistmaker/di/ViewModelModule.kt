package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.TrackAudioPlayer
import com.practicum.playlistmaker.ui.audioplayer.view_model.TrackViewModel
import com.practicum.playlistmaker.ui.library.favorites.view_model.FavouritesViewModel
import com.practicum.playlistmaker.ui.library.view_model.PlaylistViewModel
import com.practicum.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.practicum.playlistmaker.ui.setting.view_model.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (track: Track?) ->
        TrackViewModel(track, get { parametersOf(track?.previewUrl ?: "") }, get())
    }
    viewModel {
        TrackSearchViewModel(get(), get())
    }
    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel {
        FavouritesViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel()
    }


}