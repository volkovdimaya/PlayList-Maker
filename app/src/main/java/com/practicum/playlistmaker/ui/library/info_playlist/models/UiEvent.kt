package com.practicum.playlistmaker.ui.library.info_playlist.models

sealed class UiEvent {
    object TracksIsEmpty : UiEvent()
}