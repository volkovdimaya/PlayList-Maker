package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models

sealed class BottomSheetClickState {
    data class TrackAddPlaylist(val title: String) : BottomSheetClickState()
    data class TrackNotAddPlaylist(val title: String) : BottomSheetClickState()
}