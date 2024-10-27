package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTracks(term: String, consumer: TrackConsumer<List<Track>>)








}
