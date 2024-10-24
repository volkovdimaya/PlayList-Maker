package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.SearchTrackRequest
import com.practicum.playlistmaker.data.dto.SearchTrackResponse
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper

class TracksRepositoryImpl(private val networkClient : RetrofitNetworkClient) : TracksRepository {
    override fun searchTracks(term: String): List<Track> {
        val response = networkClient.doRequest(SearchTrackRequest(term))

        if (response.resultCode == 200) {
            return (response as SearchTrackResponse).results.map {
                TrackResponseMapper.map(it) }
        } else {
            return emptyList()
        }
    }
}

