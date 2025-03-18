package com.practicum.playlistmaker.ui.audioplayer.mapper


import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.models.InfoTrackShort
import com.practicum.playlistmaker.domain.player.models.TrackAudioPlayer
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun mapToTrackShort(track: Track): InfoTrackShort {
        return InfoTrackShort(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = getTime(track.trackTimeMillis),
            artworkUrl100 = track.artworkUrl100
        )
    }


    private fun getTime(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}

