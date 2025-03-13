package com.practicum.playlistmaker.domain.player.interactor


import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.player.ManagerAudioPlayer

class AudioPlayerInteractor(
    private val trackUrl: String,
    private val mediaPlayer: ManagerAudioPlayer
) : TrackPlayer {
    private var statusObserver: TrackPlayer.StatusObserver? = null

    override fun play(statusObserver: TrackPlayer.StatusObserver) {
        this.statusObserver = statusObserver
        mediaPlayer.prepare(
            trackUrl = trackUrl,
            onPrepared = {
                mediaPlayer.togglePlayback(
                    onPlay = {
                        statusObserver.onPlay()
                    },
                    onPause = {
                        statusObserver.onStop()
                    }
                )
            },
            onCompletion = {
                statusObserver.onCompletion()
            }
        )
    }

    override fun pause() {
        statusObserver?.let {
            mediaPlayer.pause {
                it.onStop()
            }
        }
    }

    override fun release() {
        mediaPlayer.release()
        statusObserver = null
    }

    override fun getProgress(): Float = mediaPlayer.getCurrentPosition() / 1000f
}