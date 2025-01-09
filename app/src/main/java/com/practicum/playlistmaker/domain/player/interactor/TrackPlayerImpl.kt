package com.practicum.playlistmaker.domain.player.interactor

import com.practicum.playlistmaker.domain.player.TrackPlayer
import android.os.Handler
import android.os.Looper

class TrackPlayerImpl(
    private val trackUrl : String,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : TrackPlayer {

    private val handler = Handler(Looper.getMainLooper())
    private var countingDownRunnable = Runnable {
        startProgressUpdates()
    }

    private var statusObserver: TrackPlayer.StatusObserver? = null

    override fun play(statusObserver: TrackPlayer.StatusObserver) {
        this.statusObserver = statusObserver

        audioPlayerInteractor.preparePlayer(
            trackUrl = trackUrl, // Здесь передавайте реальный URL трека
            onPrepared = {
                audioPlayerInteractor.togglePlayback(
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
            audioPlayerInteractor.pausePlayer {
                it.onStop()
            }
        }
    }

    override fun release() {
        audioPlayerInteractor.release()
        statusObserver = null
    }

    private fun startProgressUpdates() {
        val progress = audioPlayerInteractor.getCurrentPosition() / 1000f
        statusObserver?.onProgress(progress)
        handler.postDelayed(countingDownRunnable, SECOND)
    }

    companion object {
        private const val SECOND = 1000L
    }
}