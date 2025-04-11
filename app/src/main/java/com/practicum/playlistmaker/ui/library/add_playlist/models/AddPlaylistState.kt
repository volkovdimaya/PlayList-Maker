package com.practicum.playlistmaker.ui.library.add_playlist.models

import android.net.Uri

sealed interface AddPlaylistState {
//        data object BtnDisabled : AddPlaylistState
        data class Content(val uri: Uri? = null,val  btnEnabled: Boolean = false ) : AddPlaylistState
        data class HasValidField(val hasNotEmptyField : Boolean) : AddPlaylistState
        class CreatePlayList(val successfully: Boolean) : AddPlaylistState

//        data class BtnEnabled(val enabled: Boolean = false)
}
