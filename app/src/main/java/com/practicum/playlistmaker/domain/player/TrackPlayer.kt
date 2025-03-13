package com.practicum.playlistmaker.domain.player



interface TrackPlayer {
    fun play(statusObserver: StatusObserver)
    fun pause()
    fun release()
    fun getProgress() : Float


    interface StatusObserver {
        fun onStop()
        fun onPlay()
        fun onCompletion()
    }
}