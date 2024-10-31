package com.practicum.playlistmaker.domain.interactor


import android.media.MediaPlayer

class MediaPlayerInteractor(private val mediaPlayer: MediaPlayer) {
    private var playerState = STATE_DEFAULT

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
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
        mediaPlayer.apply {
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

    private fun startPlayer(onStart: () -> Unit) {
        mediaPlayer.let {
            if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
                it.start()
                playerState = STATE_PLAYING
                onStart()
            }
        }
    }

    fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer.let {
            if (playerState == STATE_PLAYING) {
                it.pause()
                playerState = STATE_PAUSED
                onPause()
            }
        }
    }

    fun release() {
        mediaPlayer.release()
        //mediaPlayer = null
    }

    private companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}