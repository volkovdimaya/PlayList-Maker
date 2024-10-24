package com.practicum.playlistmaker.presentation.mapper


import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.InfoTrackShort
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(track : Track) : InfoTrackShort
    {
        return InfoTrackShort(
            trackName = track.trackName ?: "", //если я сюда хочу поместить текст из res value strings name="unknown"
            artistName = track.artistName ?: "",
            trackTime = getTime(track.trackTimeMillis) ?: "00:00",
            artworkUrl100 = track.artworkUrl100 ?: ""
        )
    }

    private fun getTime(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}

