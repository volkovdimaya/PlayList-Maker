package com.practicum.playlistmaker.ui.library.models

sealed interface PlaylistState{
    data object PlaylistEmpty : PlaylistState
}