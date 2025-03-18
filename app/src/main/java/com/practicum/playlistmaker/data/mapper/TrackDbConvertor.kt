package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.models.Track

class TrackDbConvertor {
    fun map(trackDao: Track): TrackEntity {
        return with(trackDao) {
            TrackEntity(
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                trackId,
                releaseDate,
                primaryGenreName,
                collectionName,
                country,
                previewUrl
            )
        }
    }

    fun map(track: TrackEntity): Track {
        return with(track) {
            Track(
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                trackId,
                releaseDate,
                primaryGenreName,
                collectionName,
                country,
                previewUrl
            )
        }
    }
}