package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.data.add_playlist.entity.PlaylistEntity
import com.practicum.playlistmaker.data.audioplayer.entity.PlayListAndTrackEntity
import com.practicum.playlistmaker.data.audioplayer.entity.PlaylistItemEntity
import com.practicum.playlistmaker.data.audioplayer.mapper.mapPlaylistAndTrackToEntity
import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCountDataSource
import com.practicum.playlistmaker.domain.add_playlist.DbRepositoryPlaylist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DbRepositoryPlaylistimpl(private val appDatabase: AppDatabase) : DbRepositoryPlaylist {
    override suspend fun insertPlaylist(playlist: Playlist): Long {
        return appDatabase.playlistDao().insertPlaylist(mapToEntity(playlist))

    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithTrackCountDataSource>> {
        val playlists = appDatabase.playlistDao().getAllPlaylistsWithTrackCount()
        return playlists

    }

    override suspend fun addTrackToPlaylist(
        track: Track,
        playlistAndTrack: PlayListAndTrack
    ): Long {
        appDatabase.playlistAndTrackDao().addTrackToPlaylistItem(
            playlist = mapToEntity(track)
        )
        return appDatabase.playlistAndTrackDao().addTrackToPlaylistTablePlayListAndTrack(
            mapPlaylistAndTrackToEntity.map(playlistAndTrack)
        )
    }

    override fun getPlaylistInfoById(id: Long): Flow<PlaylistInfo> {
        val playlistInfo = appDatabase.playlistInfoDao().getPlaylistInfoById(id)
        return playlistInfo
    }

    override fun getTracksInPlaylist(id: Long): Flow<List<Track>> {
        val tracks = appDatabase.playlistAndTrackDao().getTracksInPlaylist(id)
            .map { trackList ->
                trackList.map { track ->
                    Track(
                        trackName = track.trackName,
                        artistName = track.artistName,
                        trackTimeMillis = track.trackTimeMillis,
                        artworkUrl100 = track.artworkUrl100,
                        collectionName = track.collectionName,
                        releaseDate = track.releaseDate,
                        primaryGenreName = track.primaryGenreName,
                        country = track.country,
                        previewUrl = track.previewUrl,
                        trackId = track.trackId
                    )
                }
            }
        return tracks


    }

    override suspend fun deleteTrack(track: Track, playlistAndTrackEntity : PlayListAndTrackEntity) {
        val trackPlaylistCount = appDatabase.playlistAndTrackDao().getTrackPlaylistCount(track.trackId)
        if (trackPlaylistCount == 1) {
            appDatabase.playlistAndTrackDao().deleteTrack(mapToEntity(track))
        }
        appDatabase.playlistAndTrackDao().deletePlayListAndTrack(playlistAndTrackEntity)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description
        )
    }

    override suspend fun deletePlaylist(id: Long) {
        appDatabase.playlistDao().deletePlaylist(id)
    }

    private fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description
        )
    }

    private fun mapToEntity(track: Track): PlaylistItemEntity {
        return PlaylistItemEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackId = track.trackId
        )

    }
}
