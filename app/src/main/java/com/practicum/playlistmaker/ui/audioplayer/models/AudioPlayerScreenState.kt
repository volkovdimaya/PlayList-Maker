package com.practicum.playlistmaker.ui.audioplayer.models

import com.practicum.playlistmaker.domain.models.Track

sealed class AudioPlayerScreenState {
    object idle : AudioPlayerScreenState()
    object Error : AudioPlayerScreenState()
    data class Content(
        val trackModel: Track,
        val isFavorite : Boolean,
    ) : AudioPlayerScreenState()
//    data class IsFavorite(val active: Boolean) : AudioPlayerScreenState()
}