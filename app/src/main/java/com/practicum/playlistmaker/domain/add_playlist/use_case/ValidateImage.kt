package com.practicum.playlistmaker.domain.add_playlist.use_case

import android.net.Uri
import android.util.Log
import com.practicum.playlistmaker.domain.add_playlist.models.ValidateError
import com.practicum.playlistmaker.domain.add_playlist.models.ValidateResult

class ValidateImage {
    fun execute(uri: Uri?): ValidateResult {
        if (uri == null) {
            return ValidateResult(
                successful = false,
                errorMessage = ValidateError.Empty
            )
        }
        return ValidateResult(successful = true)
    }
}