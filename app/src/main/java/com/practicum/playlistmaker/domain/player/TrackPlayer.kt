package com.practicum.playlistmaker.domain.player

interface TrackPlayer {
    fun play(statusObserver: StatusObserver)
    fun pause()
    fun release()


    interface StatusObserver {
        fun onProgress(progress: Float)
        fun onStop()
        fun onPlay()
        fun onCompletion()
    }
}