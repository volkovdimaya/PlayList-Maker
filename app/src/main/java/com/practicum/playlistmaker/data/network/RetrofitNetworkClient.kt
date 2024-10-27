package com.practicum.playlistmaker.data.network

import android.util.Log
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.SearchTrackRequest
import com.practicum.playlistmaker.data.dto.SearchTrackResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RetrofitNetworkClient : NetworkClient {

    private val translateBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(translateBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(SearchTrackApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto !is SearchTrackRequest) {
            return Response().apply { resultCode = 400 }
        }

        try {
            val resp = itunesService.search(dto.searchText).execute()


            //val body = resp.body() ?: Response()
            val body = resp.body() ?: Response()

            if (resp.body() == null || resp.body()?.results.isNullOrEmpty()) {
                return Response().apply {
                    resultCode = 404
                }
            }

            return body.apply { resultCode = resp.code() }
        } catch (e: Exception) {
            return Response().apply { resultCode = 400 }
        }

    }
}