package com.practicum.playlistmaker.domain.add_playlist.interactor


import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.add_playlist.DbRepositoryPlaylist
import com.practicum.playlistmaker.domain.add_playlist.PicRepository
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DbInteractorPlaylistImpl(
    private val dbRepositoryPlaylist: DbRepositoryPlaylist,
    private val picRepository: PicRepository
) :
    DbInteractorPlaylist {

    override fun insertPlaylist(playlist: Playlist): Flow<Boolean> = flow {
        val id = dbRepositoryPlaylist.insertPlaylist(playlist)
        playlist.image?.let { picRepository.savePic(it, id.toString()) }

        emit(true)
    }

    override  fun getAllPlaylists(): Flow<List<PlaylistWithTrackCount>>  =
       dbRepositoryPlaylist.getAllPlaylists()

    override fun addTrackToPlaylist(playlistAndTrack: PlayListAndTrack) : Flow<Boolean> = flow {
        val result =  dbRepositoryPlaylist.addTrackToPlaylist(playlistAndTrack)
        if (result == -1L) {
            emit(false)
            return@flow
        }
        emit(true)
    }

}