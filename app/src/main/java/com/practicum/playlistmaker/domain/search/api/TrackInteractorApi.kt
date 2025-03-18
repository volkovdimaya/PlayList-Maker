package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractorApi {
    fun searchTracks(term: String) : Flow<DataConsumer<List<Track>>>
}
