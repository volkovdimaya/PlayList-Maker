package com.practicum.playlistmaker.data.network


import com.practicum.playlistmaker.data.dto.SearchTrackRequest
import com.practicum.playlistmaker.data.dto.SearchTrackResponse
import retrofit2.Call
import retrofit2.http.GET

interface SearchTrackApi {

    @GET("search")
    fun search(searchText : SearchTrackRequest)
            : Call<SearchTrackResponse>
}