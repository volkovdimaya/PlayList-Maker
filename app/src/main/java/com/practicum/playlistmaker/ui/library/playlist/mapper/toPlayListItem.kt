package com.practicum.playlistmaker.ui.library.playlist.mapper


import com.practicum.playlistmaker.domain.add_playlist.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem

fun PlaylistWithTrackCount.toPlaylistItem(): PlaylistItem {
    return PlaylistItem(
        id = id,
        image = image,
        title = title,
        trackCount = trackCount
    )
}