package com.practicum.playlistmaker.data.search.network


import com.practicum.playlistmaker.data.search.dto.SearchTrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchTrackApi {
    @GET("search")
    fun search(
        @Query("term") searchText: String,
        @Query("entity") entity: String = "song"
    )
            : Call<SearchTrackResponse>
}

