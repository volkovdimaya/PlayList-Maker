package com.practicum.playlistmaker.domain.add_playlist


import com.practicum.playlistmaker.data.audioplayer.entity.PlayListAndTrackEntity
import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCountDataSource
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface DbRepositoryPlaylist {
   suspend fun insertPlaylist(playlist: Playlist) : Long

   fun getAllPlaylists(): Flow<List<PlaylistWithTrackCountDataSource>>

   suspend fun addTrackToPlaylist(track: Track, playlistAndTrack: PlayListAndTrack) : Long

   fun getPlaylistInfoById(id : Long) : Flow<PlaylistInfo>

   fun getTracksInPlaylist(id: Long): Flow<List<Track>>

   suspend fun deleteTrack(track: Track, playlistAndTrack: PlayListAndTrackEntity)

   suspend fun updatePlaylist(playlist: Playlist)

   suspend fun deletePlaylist(id: Long)

}