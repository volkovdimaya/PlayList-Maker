package com.practicum.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.SearchTrackRequest
import java.lang.Exception

class RetrofitNetworkClient(private val context: Context, private val itunesService: SearchTrackApi) : NetworkClient {



    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response(-1)
        }
        if (dto !is SearchTrackRequest) {
            return Response(400)
        }
        try {
            val resp = itunesService.search(dto.searchText).execute()

            val body = resp.body() ?: Response()

            if (resp.body() == null || resp.body()?.results.isNullOrEmpty()) {
                return Response(404)
            }

            return body.apply { resultCode = resp.code() }
        } catch (e: Exception) {
            return Response(400)
        }

    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}