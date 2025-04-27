package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.mapper

import com.practicum.playlistmaker.domain.add_playlist.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.PlaylistBottomSheetItem
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist

fun PlaylistWithTrackCount.tiPlaylistBottomSheetItem(): PlaylistBottomSheetItem {
    return PlaylistBottomSheetItem(
        id = id,
        title = title,
        image = image,
        trackCount = trackCount
    )
}
fun PlaylistBottomSheetItem.toPlaylist(): Playlist {
    return Playlist(
        id = id,
    )
}