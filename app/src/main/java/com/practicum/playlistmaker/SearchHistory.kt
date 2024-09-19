package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val PLAYLIST_MAKER = "PLAYLIST_MAKER"
const val MODE_THEME = "MODE_THEME"
const val HISTORY_LIST_TRACK = "HISTORY_LIST_TRACK"

class SearchHistory(val sharedPrefs : SharedPreferences,

) {

    var songs : List<Track> = emptyList()
    var hasHistory : Boolean = false
    fun read() {
        var json = sharedPrefs.getString(HISTORY_LIST_TRACK, null)
        songs = json?.let {
            Gson().fromJson(it, object : TypeToken<List<Track>>() {}.type)
        } ?: emptyList()
        hasHistory = songs.isNotEmpty()

    }

    fun write() {
        val json = Gson().toJson(songs)
        sharedPrefs.edit()
            .putString(HISTORY_LIST_TRACK, json)
            .apply()
    }

    fun clear()
    {
        songs = emptyList()
        sharedPrefs.edit()
            .remove(HISTORY_LIST_TRACK)
            .apply()
    }

    fun update(model: Track)
    {

        val trackIndex = songs.indexOfFirst { it.trackId.equals( model.trackId) }

        if (trackIndex != -1) {
            val mutableTracks = songs.toMutableList()
            val track = mutableTracks.removeAt(trackIndex)
            mutableTracks.add(track)
            songs = mutableTracks.toList()
            hasHistory = songs.isNotEmpty()
        }
        val size = songs.size
        val mutableTracks = songs.toMutableList()
        if(size == 10)
            mutableTracks.removeAt(0)
        mutableTracks.add(model)
        songs =  mutableTracks.toList()
        hasHistory = songs.isNotEmpty()
    }
}