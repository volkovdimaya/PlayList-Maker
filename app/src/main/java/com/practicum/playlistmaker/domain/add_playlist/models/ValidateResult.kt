package com.practicum.playlistmaker.domain.add_playlist.models

data class ValidateResult(
    val successful: Boolean,
    val errorMessage : ValidateError? = null
)