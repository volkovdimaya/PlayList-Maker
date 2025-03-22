package com.practicum.playlistmaker.domain.db


import com.practicum.playlistmaker.domain.db.model.DataFavorite
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface InteractorFavorite {
    fun isFavorite(track : Track) : Flow<Boolean>
    fun clickFavorite(track : Track) : Flow<DataFavorite>
}