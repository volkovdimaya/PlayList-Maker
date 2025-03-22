package com.practicum.playlistmaker.domain.db.model

sealed class DataFavorite {
    data object Add : DataFavorite()
    data object Delete : DataFavorite()
}