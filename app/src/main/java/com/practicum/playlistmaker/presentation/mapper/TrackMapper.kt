package com.practicum.playlistmaker.presentation.mapper


import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.InfoTrackShort
import com.practicum.playlistmaker.presentation.models.TrackAudioPlayer
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
    fun mapToTrackAudioPlayer(track: Track): TrackAudioPlayer {
        return TrackAudioPlayer(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.releaseDate,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.previewUrl
        )
    }

    private fun getTime(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}

