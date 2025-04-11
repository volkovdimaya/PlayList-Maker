package com.practicum.playlistmaker.domain.add_playlist.use_case

import com.practicum.playlistmaker.domain.add_playlist.models.ValidateError
import com.practicum.playlistmaker.domain.add_playlist.models.ValidateResult

class ValidateDescription() {
    fun execute(description: String): ValidateResult {
        if (description.isBlank()) {
            return ValidateResult(
                successful = false,
                errorMessage = ValidateError.Empty
            )
        }
        return ValidateResult(successful = true)
    }
}