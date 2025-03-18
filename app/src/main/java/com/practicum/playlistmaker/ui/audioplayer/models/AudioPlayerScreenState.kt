package com.practicum.playlistmaker.ui.audioplayer.models

import com.practicum.playlistmaker.domain.models.Track

sealed class AudioPlayerScreenState {
    object Error : AudioPlayerScreenState()
    data class Content(
        val trackModel: Track,
    ) : AudioPlayerScreenState()
    data class IsFavorite(val active: Boolean) : AudioPlayerScreenState()
}