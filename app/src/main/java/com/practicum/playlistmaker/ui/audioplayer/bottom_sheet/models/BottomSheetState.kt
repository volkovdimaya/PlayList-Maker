package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models


sealed class BottomSheetState {
    object idle : BottomSheetState()
    data class Content(val playlistItem: List<PlaylistBottomSheetItem>) : BottomSheetState()
}
