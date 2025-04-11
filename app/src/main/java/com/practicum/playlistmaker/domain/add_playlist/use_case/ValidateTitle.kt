package com.practicum.playlistmaker.domain.add_playlist.use_case

import com.practicum.playlistmaker.domain.add_playlist.models.ValidateError
import com.practicum.playlistmaker.domain.add_playlist.models.ValidateResult

class ValidateTitle() {
    fun execute(name: String): ValidateResult {
        if (name.isBlank()) {
            return ValidateResult(
                successful = false,
                errorMessage = ValidateError.Empty
            )
        }
        return ValidateResult(successful = true)
    }
}