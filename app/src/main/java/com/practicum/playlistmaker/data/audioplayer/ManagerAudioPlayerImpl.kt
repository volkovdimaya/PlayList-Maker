package com.practicum.playlistmaker.data.audioplayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.ManagerAudioPlayer

class ManagerAudioPlayerImpl(private val mediaPlayer: MediaPlayer) : ManagerAudioPlayer {

    private var playerState = STATE_DEFAULT

    override fun prepare(
        trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit
    ) {
        mediaPlayer.apply {
            if (isPlaying || playerState == STATE_PREPARED || playerState ==  STATE_PAUSED) {
                onPrepared()
                return
            }

            setDataSource(trackUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
                onPrepared()
            }
            setOnCompletionListener {
                pause {
                    playerState = STATE_PAUSED
                    onCompletion()
                }
            }
        }
    }

    override fun pause(onPause: () -> Unit) {
        mediaPlayer.let {
            if (playerState == STATE_PLAYING) {
                it.pause()
                playerState = STATE_PAUSED
                onPause()
            }
        }
    }

    override fun play(onStart: () -> Unit) {
        mediaPlayer.let {
            if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
                it.start()
                playerState = STATE_PLAYING
                onStart()
            }
        }


    }

    override fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit) {
        when (playerState) {
            STATE_PLAYING -> {
                pause(onPause)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                play(onPlay)
            }
        }
    }



    override fun stop() {
        mediaPlayer.stop()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition

    }

    override fun release() {
        mediaPlayer.release()
    }

    private companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}