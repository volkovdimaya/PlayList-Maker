package com.practicum.playlistmaker.ui.library.info_playlist.models

import android.net.Uri

data class PlaylistInfo(
    val title : String,
    val description : String,
    val image : Uri? = null,
    val count : Int,
    val duration: Long
)
