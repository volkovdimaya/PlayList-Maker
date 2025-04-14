package com.practicum.playlistmaker.data.db.models

import android.net.Uri

data class PlaylistWithTrackCount(
    val id: Long,
    val image: Uri?,
    val title: String,
    val trackCount: Int
)