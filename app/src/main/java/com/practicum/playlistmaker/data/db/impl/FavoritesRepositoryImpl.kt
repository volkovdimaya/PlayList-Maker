package com.practicum.playlistmaker.data.db.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.data.mapper.TrackDbConvertor
import com.practicum.playlistmaker.domain.db.FavoritesRepository
import com.practicum.playlistmaker.domain.models.Track

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun delete(track: Track) {
        appDatabase.trackDao().delete(trackDbConvertor.map(track))
    }

    override suspend fun loadFavoriteTrack(id: String): Track? {
        return appDatabase.trackDao().getTrackFavorite(id)?.let { trackDbConvertor.map(it) }
    }

    override suspend fun favoritesTracks(): List<Track> {
        return appDatabase.trackDao().getTracksFavorites()
            .let {
                convertFromTrackEntity(it)
            }

    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}