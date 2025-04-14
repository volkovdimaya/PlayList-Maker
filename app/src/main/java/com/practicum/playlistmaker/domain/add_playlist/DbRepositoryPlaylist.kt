package com.practicum.playlistmaker.domain.add_playlist


import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

interface DbRepositoryPlaylist {
   suspend fun insertPlaylist(playlist: Playlist) : Long

   fun getAllPlaylists(): Flow<List<PlaylistWithTrackCount>>

   suspend fun addTrackToPlaylist(playlistAndTrack: PlayListAndTrack) : Long

}