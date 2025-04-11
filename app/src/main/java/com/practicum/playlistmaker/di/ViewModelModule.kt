package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model.AudioPlayerEventFromBottomSheet
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model.BottomSheetViewModel
import com.practicum.playlistmaker.ui.audioplayer.view_model.TrackViewModel
import com.practicum.playlistmaker.ui.library.favorites.view_model.FavouritesViewModel
import com.practicum.playlistmaker.ui.library.add_playlist.view_model.AddPlayListviewModel
import com.practicum.playlistmaker.ui.library.playlist.view_model.PlaylistViewModel
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
        FavouritesViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }
    viewModel {
        BottomSheetViewModel(get())
    }
    viewModel{
        AddPlayListviewModel(get(), get(), get(), get())
    }
    viewModel{
        AudioPlayerEventFromBottomSheet()
    }


}