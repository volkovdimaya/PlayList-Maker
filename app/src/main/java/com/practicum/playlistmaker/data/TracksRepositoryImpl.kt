package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.SearchTrackRequest
import com.practicum.playlistmaker.data.dto.SearchTrackResponse
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper
import com.practicum.playlistmaker.domain.models.Resource

class TracksRepositoryImpl(
    private val networkClient: RetrofitNetworkClient,
    private val trackResponseMapper: TrackResponseMapper
) : TracksRepository {

    override fun searchTracks(term: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchTrackRequest(term))
        return when (response.resultCode) {
            200 -> (response as SearchTrackResponse)
                .results.map(trackResponseMapper::map)
                .let { Resource.Success(it) }

            404 -> Resource.Error(404)
            else -> Resource.Error(400)

        }
    }
}

