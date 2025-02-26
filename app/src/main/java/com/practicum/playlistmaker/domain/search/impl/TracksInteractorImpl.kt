package com.practicum.playlistmaker.domain.impl

import android.util.Log
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TrackInteractorApi {

    private val executor = Executors.newCachedThreadPool()
//    override fun searchTracks(term: String, consumer: TrackConsumer<List<Track>>) {
    override fun searchTracks(term: String) : Flow<DataConsumer<List<Track>>> {



        return repository.searchTracks(term).map { result ->
            when(result){
                is Resource.Success ->{
                    DataConsumer.Success(result.data)
                }

                is Resource.Error -> when(result.error){
                    404 -> DataConsumer.ResponseNoContent()
                    else -> DataConsumer.ResponseFailure()
                }
            }

        }
//        executor.execute {
//            val respons = repository.searchTracks(term)
//            when (respons) {
//                is Resource.Success -> {
//                    consumer.consume(DataConsumer.Success(respons.data))
//                }
//
//                is Resource.Error -> {
//                    if (respons.error == 400) {
//                        consumer.consume(DataConsumer.ResponseFailure())
//                    }
//                    if (respons.error == 404) {
//                        consumer.consume(DataConsumer.ResponseNoContent())
//                    }
//                }
//            }
//        }
    }
}


