package com.practicum.playlistmaker.domain.api

interface TrackConsumer<T> {
    fun consume(data: DataConsumer<T>)
    //fun consume(foundTrack: Resource<List<Track>>)
}