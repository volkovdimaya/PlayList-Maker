package com.practicum.playlistmaker.domain.add_playlist


import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

interface DbInteractorPlaylist {
    fun insertPlaylist(playlist: Playlist): Flow<Boolean>
    fun getAllPlaylists(): Flow<List<PlaylistWithTrackCount>>
    fun addTrackToPlaylist(playlistAndTrack: PlayListAndTrack): Flow<Boolean>
}