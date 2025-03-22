package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface UseCaseGetFavoritesTracks {
    fun execute() : Flow<List<Track>>
}