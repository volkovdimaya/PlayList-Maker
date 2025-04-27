package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model.AudioPlayerEventFromBottomSheet
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model.BottomSheetViewModel
import com.practicum.playlistmaker.ui.audioplayer.view_model.TrackViewModel
import com.practicum.playlistmaker.ui.library.favorites.view_model.FavouritesViewModel
import com.practicum.playlistmaker.ui.library.add_playlist.view_model.AddPlayListviewModel
import com.practicum.playlistmaker.ui.library.info_playlist.bottom_sheet.view_model.BottomSheetMoreViewModel
import com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist.view_model.EditPlaylistViewModel
import com.practicum.playlistmaker.ui.library.info_playlist.view_model.PlaylistInfoViewModel
import com.practicum.playlistmaker.ui.library.playlist.view_model.PlaylistViewModel
import com.practicum.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.practicum.playlistmaker.ui.setting.view_model.SettingViewModel
import com.practicum.playlistmaker.ui.share_data.SharedPlaylistViewModel
import com.practicum.playlistmaker.ui.share_data.SharedTrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

//    viewModel { (track: Track?) ->
    viewModel {
        TrackViewModel( get(), get())
//        TrackViewModel(track, get { parametersOf(track?.previewUrl ?: "") }, get())
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
    viewModel{
        PlaylistInfoViewModel(get(), get())
    }
    viewModel{
        BottomSheetMoreViewModel(get(), get())
    }
    viewModel{
        SharedTrackViewModel()
    }
    viewModel{
        SharedPlaylistViewModel()
    }
    viewModel{
        EditPlaylistViewModel(get(), get(), get(), get())
    }


}