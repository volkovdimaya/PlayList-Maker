package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.data.dto.SearchTrackResponse
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.InfoTrackShort

object TrackResponseMapper {
    fun map(track : TrackDto) : Track
    {
        return Track(
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
}