package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.player.models.TrackAudioPlayer
import com.practicum.playlistmaker.ui.audioplayer.view_model.TrackViewModel
import com.practicum.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.practicum.playlistmaker.ui.setting.view_model.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {(track: TrackAudioPlayer?)->
        TrackViewModel(track, get { parametersOf(track?.previewUrl ?: "") })
    }
    viewModel {
        TrackSearchViewModel(get(), get())//тут проблема
    }
    viewModel {
        SettingViewModel(get(), get())
    }



}