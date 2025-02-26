package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.search.dto.SearchTrackRequest
import com.practicum.playlistmaker.data.search.dto.SearchTrackResponse
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper
import com.practicum.playlistmaker.data.search.network.NetworkClient
import com.practicum.playlistmaker.domain.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackResponseMapper: TrackResponseMapper
) : TracksRepository {

    override fun searchTracks(term: String): Flow<Resource<List<Track>>>  = flow{
        val response = networkClient.doRequest(SearchTrackRequest(term))
//        return when (response.resultCode) {
        when (response.resultCode) {
            200 -> (response as SearchTrackResponse)
                .results.map(trackResponseMapper::map)
                .let { emit(Resource.Success(it)) }

            404 -> emit(Resource.Error(404))
            else -> emit(Resource.Error(400))

        }
    }
}

