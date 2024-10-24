package com.practicum.playlistmaker.data.network

import android.util.Log
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.SearchTrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val translateBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(translateBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(SearchTrackApi::class.java)

    override fun doRequest(dto: Any): Response {
        Log.d("1212121212","12 ${dto.toString()}")
        if (dto is SearchTrackRequest) {
            try {
                val resp = itunesService.search(dto).execute()
                Log.d("1212121212","12 ${resp.toString()}")

                val body = resp.body() ?: Response()

                return body.apply { resultCode = resp.code() }
            }catch (e : Exception)
            {
                Log.d("1212121212","${e.toString()}")
                return Response().apply { resultCode = 400 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}