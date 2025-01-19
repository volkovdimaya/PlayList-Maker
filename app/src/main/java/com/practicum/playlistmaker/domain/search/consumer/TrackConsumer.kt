package com.practicum.playlistmaker.domain.consumer

interface TrackConsumer<T> {
    fun consume(data: DataConsumer<T>)
}