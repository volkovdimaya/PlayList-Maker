package com.practicum.playlistmaker.domain.add_playlist


import com.practicum.playlistmaker.domain.add_playlist.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

interface DbInteractorPlaylist {
    fun insertPlaylist(playlist: Playlist): Flow<Boolean>
    fun getAllPlaylists(): Flow<List<PlaylistWithTrackCount>>
    fun addTrackToPlaylist(track: Track, playlistAndTrack: PlayListAndTrack): Flow<Boolean>
    fun getPlaylistInfoById(id : Long) : Flow<PlaylistInfo?>
    fun getTrackInPlaylist(id: Long): Flow<List<Track>>
    suspend fun deleteTrack(track: Track, playlistAndTrack: PlayListAndTrack)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(id: Long)
}