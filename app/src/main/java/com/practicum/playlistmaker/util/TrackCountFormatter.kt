package com.practicum.playlistmaker.util

object TrackCountFormatter {

    fun formatTrackCount(count: Int): String {
        return "$count ${pluralizeTracks(count)}"
    }

   private fun pluralizeTracks(count: Int): String {
        return when (count) {
            1 -> "трек"
            in 2..4 -> "трека"
            else -> "треков"
        }
    }
}