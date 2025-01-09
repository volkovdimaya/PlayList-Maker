package com.practicum.playlistmaker.domain.player.interactor


import com.practicum.playlistmaker.domain.player.ManagerAudioPlayer

class AudioPlayerInteractor(private val mediaPlayer: ManagerAudioPlayer) {
    fun getCurrentPosition(): Int {
        return mediaPlayer.getCurrentPosition()
    }

    fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit) {
        mediaPlayer.togglePlayback(onPlay, onPause)
    }


    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.prepare(trackUrl, onPrepared, onCompletion)
    }

    private fun startPlayer(onStart: () -> Unit) {
        mediaPlayer.play(onStart)
    }

    fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer.pause(onPause)
    }

    fun release() {
        mediaPlayer.release()
    }
}