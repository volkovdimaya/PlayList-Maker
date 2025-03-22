package com.practicum.playlistmaker.domain.db.use_case

import com.practicum.playlistmaker.domain.db.FavoritesRepository
import com.practicum.playlistmaker.domain.db.UseCaseGetFavoritesTracks
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UseCaseGetFavoritesTracksImpl(private val favoritesRepository: FavoritesRepository) : UseCaseGetFavoritesTracks {
    override fun execute(): Flow<List<Track>>  = flow{
        emit(favoritesRepository.favoritesTracks())
    }
}