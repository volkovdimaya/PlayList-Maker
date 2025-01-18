package com.practicum.playlistmaker.domain.player

interface PlayerTimer {
    fun postDelayed(delayMillis: Long, action: Runnable)
    fun removeCallbacks(action: Runnable)
}