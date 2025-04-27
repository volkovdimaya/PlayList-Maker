package com.practicum.playlistmaker.ui.library.info_playlist.api



interface UseCasePlaylistShare {
    suspend fun sharePlaylist(id: Long) : Boolean

}