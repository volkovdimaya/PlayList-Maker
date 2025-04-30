package com.practicum.playlistmaker.ui.library.add_playlist.models

import android.net.Uri

sealed interface AddPlaylistState {
    data class Content(
        val uri: Uri? = null,
        val btnEnabled: Boolean = false
    ) : AddPlaylistState

    data class HasValidField(val hasNotEmptyField: Boolean) : AddPlaylistState
    class CreatePlayList(val successfully: Boolean) : AddPlaylistState
}
