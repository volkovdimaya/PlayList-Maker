package com.practicum.playlistmaker.domain.player.interactor


import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.player.ManagerAudioPlayer
import com.practicum.playlistmaker.domain.player.PlayerTimer

class AudioPlayerInteractor(
    private val trackUrl : String,
    private val mediaPlayer: ManagerAudioPlayer,
    private  val playerTimer : PlayerTimer
) : TrackPlayer {

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
                statusObserver.onCompletion()
            }
        )
    }

    override fun pause() {
        statusObserver?.let {
            playerTimer.removeCallbacks(countingDownRunnable)
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
        playerTimer.postDelayed(SECOND, countingDownRunnable)
    }

    companion object {
        private const val SECOND = 1000L
    }
}