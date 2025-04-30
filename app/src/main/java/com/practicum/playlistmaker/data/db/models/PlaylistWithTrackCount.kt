package com.practicum.playlistmaker.data.db.models


data class PlaylistWithTrackCountDataSource(
    val id: Long,
    val title: String,
    val trackCount: Int
)