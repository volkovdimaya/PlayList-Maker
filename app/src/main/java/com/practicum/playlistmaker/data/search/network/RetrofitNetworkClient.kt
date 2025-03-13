package com.practicum.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.SearchTrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

class RetrofitNetworkClient(
    private val context: Context,
    private val itunesService: SearchTrackApi
) : NetworkClient {


    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response(-1)
        }
        if (dto !is SearchTrackRequest) {

            return Response(400)
        }
        return withContext(Dispatchers.IO) {
            try {
                val resp = itunesService.search(dto.searchText)
                if (resp.results.isEmpty()) {
                    return@withContext Response().apply { resultCode = 404 }
                }
                resp.apply { resultCode = 200 }
            } catch (e: HttpException) {
                Response().apply { resultCode = e.code() }
            } catch (e: Throwable) {
                Response().apply { resultCode = 400 }
            }
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