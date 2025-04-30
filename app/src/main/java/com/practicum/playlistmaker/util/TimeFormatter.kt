package com.practicum.playlistmaker.util

object TimeFormatter {
    fun formatDuration(durationMillis: Long): String {
        val minutes = durationMillis / 60000
        return "$minutes ${pluralizeMinutes(minutes)}"
    }

    private fun pluralizeMinutes(minutes: Long): String {
        val m = minutes % 100
        return when {
            m in 11..14 -> "минут"
            (m % 10).toInt() == 1 -> "минута"
            m % 10 in 2..4 -> "минуты"
            else -> "минут"
        }
    }
}