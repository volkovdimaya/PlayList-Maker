package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


const val HISTORY_LIST_TRACK = "HISTORY_LIST_TRACK"
const val MODE_THEME = "MODE_THEME"
const val PLAYLIST_MAKER = "PLAYLIST_MAKER"

class SearchHistory(
    val sharedPrefs: SharedPreferences
    ) {

    private var songs: List<Track> = emptyList()
    private val gson = Gson()
    var hasHistory: Boolean = false
        get() = songs.isNotEmpty()

    fun read() {
        val json = sharedPrefs.getString(HISTORY_LIST_TRACK, null)
        songs = json?.let {
            gson.fromJson(it, object : TypeToken<List<Track>>() {}.type)
        } ?: emptyList()
        hasHistory = songs.isNotEmpty()
    }

    fun getSong(): List<Track> {
        return songs
    }


    private fun write() {
        val json = gson.toJson(songs)
        sharedPrefs.edit()
            .putString(HISTORY_LIST_TRACK, json)
            .apply()
    }

    fun clear() {
        songs = emptyList()
        sharedPrefs.edit()
            .remove(HISTORY_LIST_TRACK)
            .apply()
    }

    fun update(model: Track) {
        val trackIndex = songs.indexOfFirst { it.trackId.equals(model.trackId) }
        val mutableTracks = songs.toMutableList()
        if (trackIndex != -1) {
            val track = mutableTracks.removeAt(trackIndex)
            mutableTracks.add(track)
        } else {
            val size = songs.size

            if (size == 10)
                mutableTracks.removeAt(0)
            mutableTracks.add(model)
        }
        songs = mutableTracks.toList()
        write()
    }
}