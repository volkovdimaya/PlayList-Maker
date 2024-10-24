package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.RepositorySearchHistory


const val HISTORY_LIST_TRACK = "HISTORY_LIST_TRACK"
const val MODE_THEME = "MODE_THEME"
const val PLAYLIST_MAKER = "PLAYLIST_MAKER"


class RepositorySearchHistoryImpl(
    val sharedPrefs: SharedPreferences
) : RepositorySearchHistory{

    private var songs: List<TrackDto> = emptyList()
    private val gson = Gson()
    override fun hasHistory() : Boolean
    {
        read()
        return songs.isNotEmpty()
    }

    fun read() {
        val json = sharedPrefs.getString(HISTORY_LIST_TRACK, null)
        songs = json?.let {
            gson.fromJson(it, object : TypeToken<List<TrackDto>>() {}.type)
        } ?: emptyList()
        //hasHistory = songs.isNotEmpty()
    }

    override fun getSong(): List<Track> {
        read()
        return songs.map { Track(it.trackName,
            it.artistName,
            it.trackTimeMillis,
            it.artworkUrl100,
            it.trackId,
            it.releaseDate,
            it.primaryGenreName,
            it.collectionName,
            it.country,
            it.previewUrl
        ) }
    }


    private fun write() {
        val json = gson.toJson(songs)
        sharedPrefs.edit()
            .putString(HISTORY_LIST_TRACK, json)
            .apply()
    }

    override fun clear() {
        songs = emptyList()
        sharedPrefs.edit()
            .remove(HISTORY_LIST_TRACK)
            .apply()
    }

    override fun update(track : Track) {
        val model = TrackDto(track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.releaseDate,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.previewUrl)

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