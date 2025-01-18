package com.practicum.playlistmaker.data.audioplayer

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.domain.player.PlayerTimer

class PlayerTimerImpl : PlayerTimer {
    private val handler = Handler(Looper.getMainLooper())

    override fun postDelayed(delayMillis: Long, action: Runnable) {
        handler.postDelayed(action, delayMillis)
    }

    override fun removeCallbacks(action: Runnable) {
        handler.removeCallbacks(action)
    }
}