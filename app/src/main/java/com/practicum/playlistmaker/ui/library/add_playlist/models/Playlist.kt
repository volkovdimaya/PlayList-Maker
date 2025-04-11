package com.practicum.playlistmaker.ui.library.add_playlist.models

import android.net.Uri

data class Playlist(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val image: Uri? = null
)