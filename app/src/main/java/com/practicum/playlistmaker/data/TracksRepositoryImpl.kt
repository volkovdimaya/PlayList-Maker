package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.SearchTrackRequest
import com.practicum.playlistmaker.data.dto.SearchTrackResponse
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper
import com.practicum.playlistmaker.domain.models.Resource

class TracksRepositoryImpl(private val networkClient: RetrofitNetworkClient) : TracksRepository {

    override fun searchTracks(term: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchTrackRequest(term))

        when (response.resultCode) {
            200 -> {
                val tracks = (response as SearchTrackResponse).results.map {

                    TrackResponseMapper.map(it)
                }
                return Resource.Success(tracks)
            }

            404 -> {
                return Resource.Error(404)
            }

            else -> {
                return Resource.Error(400)
            }
        }
    }
}

