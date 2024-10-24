package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTracks(term : String, consumer : TrackCunsumer)

    interface TrackCunsumer {
        fun consume(foundTrack: List<Track>)
    }
}