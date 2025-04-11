package com.practicum.playlistmaker.ui.library.add_playlist.models

import android.net.Uri

sealed interface AddPlaylistState {
        data object BtnDisabled : AddPlaylistState
        data object BtnEnabled : AddPlaylistState
        data class ShowPic(val uri: Uri) : AddPlaylistState
        data class HasValidField(val hasNotEmptyField : Boolean) : AddPlaylistState
        class CreatePlayList(val successfully: Boolean) : AddPlaylistState
}
