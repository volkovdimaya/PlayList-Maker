package com.practicum.playlistmaker.ui.library.playlist.models

sealed interface PlaylistState{
    data object PlaylistEmpty : PlaylistState
    class PlaylistContent(val playlist: List<PlaylistItem>) : PlaylistState
}