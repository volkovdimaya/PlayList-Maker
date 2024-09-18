package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiRetrofit {
    private val translateBaseUrl = "https://itunes.apple.com/"

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(translateBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}


data class SearchTrackResponse(val results: List<Track>)


interface SearchTrackApi {

    @GET("search")
    fun search(
        @Query("term") searchText: String,
        @Query("entity") entity: String = "song"
    )
            : Call<SearchTrackResponse>
}




