package com.practicum.playlistmaker.domain.player.interactor

import com.practicum.playlistmaker.domain.player.TrackPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.domain.player.ManagerAudioPlayer

class AudioPlayerInteractor(
    private val trackUrl : String,
    private val mediaPlayer: ManagerAudioPlayer
) : TrackPlayer {

    private val handler = Handler(Looper.getMainLooper())
    private var countingDownRunnable = Runnable {
        startProgressUpdates()
    }

    private var statusObserver: TrackPlayer.StatusObserver? = null

    override fun play(statusObserver: TrackPlayer.StatusObserver) {
        this.statusObserver = statusObserver

        mediaPlayer.prepare(
            trackUrl = trackUrl,
            onPrepared = {
                mediaPlayer.togglePlayback(
                    onPlay = {
                        statusObserver.onPlay()
                        startProgressUpdates()
                    },
                    onPause = {
                        statusObserver.onStop()
                    }
                )
            },
            onCompletion = {
                statusObserver.onStop()
            }
        )
    }

    override fun pause() {
        statusObserver?.let {
            handler.removeCallbacks(countingDownRunnable)
            mediaPlayer.pause {
                it.onStop()
            }
        }
    }

    override fun release() {
        mediaPlayer.release()
        statusObserver = null
    }

    private fun startProgressUpdates() {
        val progress = mediaPlayer.getCurrentPosition() / 1000f
        statusObserver?.onProgress(progress)
        handler.postDelayed(countingDownRunnable, SECOND)
    }

    companion object {
        private const val SECOND = 1000L
    }
}