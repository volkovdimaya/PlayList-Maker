package com.practicum.playlistmaker.domain.interactor


import android.media.MediaPlayer

class MediaPlayerInteractor {
    private var mediaPlayer: MediaPlayer? = null
    private var playerState = STATE_DEFAULT


    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit) {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer(onPause)
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(onPlay)
            }
        }
    }

    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(trackUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
                onPrepared()
            }
            setOnCompletionListener {
                pausePlayer {
                    playerState = STATE_PAUSED
                    onCompletion()
                }
            }
        }
    }

    fun startPlayer(onStart: () -> Unit) {
        mediaPlayer?.let {
            if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
                it.start()
                playerState = STATE_PLAYING
                onStart()
            }
        }
    }

    fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer?.let {
            if (playerState == STATE_PLAYING) {
                it.pause()
                playerState = STATE_PAUSED
                onPause()
            }
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}