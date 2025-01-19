package com.practicum.playlistmaker.ui.audioplayer.models
import com.practicum.playlistmaker.domain.player.models.TrackAudioPlayer

sealed class AudioPlayerScreenState {
    object Error: AudioPlayerScreenState()
    data class Content(
        val trackModel: TrackAudioPlayer,
    ): AudioPlayerScreenState()
}