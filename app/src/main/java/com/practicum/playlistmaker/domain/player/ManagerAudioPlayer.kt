package com.practicum.playlistmaker.domain.player

interface ManagerAudioPlayer {
    fun prepare(
        trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit
    )
    fun play(onStart: () -> Unit)
    fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit)
    fun pause(onPause: () -> Unit)
    fun stop()
    fun getCurrentPosition(): Int
    fun release()
}