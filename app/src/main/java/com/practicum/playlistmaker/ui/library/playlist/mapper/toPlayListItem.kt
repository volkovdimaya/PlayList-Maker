package com.practicum.playlistmaker.ui.library.playlist.mapper

import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem

fun PlaylistWithTrackCount.toPlaylistItem(): PlaylistItem {
    return PlaylistItem(
        image = image,
        title = title,
        trackCount = trackCount
    )
}