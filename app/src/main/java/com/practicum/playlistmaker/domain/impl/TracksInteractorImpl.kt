package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()


    override fun searchTracks(term: String, consumer: TrackConsumer<List<Track>>) {
        executor.execute {
            val respons = repository.searchTracks(term)
            when (respons) {
                is Resource.Success -> {
                    consumer.consume(DataConsumer.Success(respons.data))
                }

                is Resource.Error -> {
                    if (respons.error == 400) {
                        consumer.consume(DataConsumer.ResponseFailure())
                    }
                    if (respons.error == 404) {
                        consumer.consume(DataConsumer.ResponseNoContent())
                    }
                }
            }


        }


    }
}


