package com.practicum.playlistmaker.domain.api


import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(term: String): Resource<List<Track>>

}