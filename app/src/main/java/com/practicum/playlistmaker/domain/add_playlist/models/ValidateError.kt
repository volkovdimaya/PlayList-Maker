package com.practicum.playlistmaker.domain.add_playlist.models

sealed class ValidateError {
    data object Empty : ValidateError()
    data object Invalid : ValidateError()
}