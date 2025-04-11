package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.data.add_playlist.entity.PlaylistEntity
import com.practicum.playlistmaker.data.audioplayer.mapper.mapPlaylistAndTrackToEntity
import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.domain.add_playlist.DbRepositoryPlaylist
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

class DbRepositoryPlaylistimpl(private val appDatabase: AppDatabase) : DbRepositoryPlaylist {
    override suspend fun insertPlaylist(playlist: Playlist): Long {
        return appDatabase.playlistDao().insertPlaylist(mapToEntity(playlist))

    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithTrackCount>> {
        val playlists = appDatabase.playlistDao().getAllPlaylistsWithTrackCount()
        return playlists

    }

    override suspend fun addTrackToPlaylist(playlistAndTrack: PlayListAndTrack) : Long {
       return appDatabase.playlistAndTrackDao().addTrackToPlaylistById(
            mapPlaylistAndTrackToEntity.map(playlistAndTrack)
        )
    }

    private fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            image = playlist.image
        )
    }
}
