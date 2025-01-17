package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.data.mapper.TrackResponseMapper
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.RepositorySearchHistory
import androidx.core.content.edit
import com.practicum.playlistmaker.data.mapper.TrackDtoResponseMapper


const val HISTORY_LIST_TRACK = "HISTORY_LIST_TRACK"
const val MODE_THEME = "MODE_THEME"
const val PLAYLIST_MAKER = "PLAYLIST_MAKER"


class RepositorySearchHistoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val trackDtoResponseMapper: TrackDtoResponseMapper
) : RepositorySearchHistory {

    private var songs: List<TrackDto> = emptyList()
    private val gson = Gson()
    override fun hasHistory(): Boolean {
        read()
        return songs.isNotEmpty()
    }

    private fun read() {
        val json = sharedPrefs.getString(HISTORY_LIST_TRACK, null)
        songs = json?.let {
            gson.fromJson(it, object : TypeToken<List<TrackDto>>() {}.type)
        } ?: emptyList()

    }

    override fun getSong(): List<Track> {
        read()
        return songs.map {
            TrackResponseMapper.map(it)
        }
    }


    private fun write() {
        val json = gson.toJson(songs)
        sharedPrefs.edit { putString(HISTORY_LIST_TRACK, json) }
    }

    override fun clear() {
        songs = emptyList()
        sharedPrefs.edit { remove(HISTORY_LIST_TRACK) }

    }

    override fun update(track: Track) {

        val model = trackDtoResponseMapper.map(track)

        read()
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