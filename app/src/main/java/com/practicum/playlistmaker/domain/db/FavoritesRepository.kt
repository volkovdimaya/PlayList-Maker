package com.practicum.playlistmaker.domain.db


import com.practicum.playlistmaker.domain.models.Track

interface FavoritesRepository {
    suspend fun delete(track: Track)
    suspend fun addTrack(track: Track)
    suspend fun loadFavoriteTrack(id : String) : Track?
    suspend fun favoritesTracks() : List<Track>
}