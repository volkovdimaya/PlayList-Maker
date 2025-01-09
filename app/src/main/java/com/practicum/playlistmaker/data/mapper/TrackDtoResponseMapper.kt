package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track

object TrackDtoResponseMapper {
    fun map(track: Track): TrackDto {
        return TrackDto(
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