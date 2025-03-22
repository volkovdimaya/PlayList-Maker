package com.practicum.playlistmaker.domain.db.use_case


import com.practicum.playlistmaker.domain.db.FavoritesRepository
import com.practicum.playlistmaker.domain.db.InteractorFavorite
import com.practicum.playlistmaker.domain.db.model.DataFavorite
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InteractorFavoriteImpl(private val favoritesRepository: FavoritesRepository) : InteractorFavorite {
    override fun isFavorite(track: Track): Flow<Boolean> = flow{
      val isTrack =  favoritesRepository.loadFavoriteTrack(track.trackId)
        emit(isTrack != null)

    }

    override fun clickFavorite(track: Track): Flow<DataFavorite> = flow{
        val isTrack =  favoritesRepository.loadFavoriteTrack(track.trackId)
        if (isTrack != null){
           favoritesRepository.delete(track)
           emit(DataFavorite.Delete)
       }else{
           favoritesRepository.addTrack(track)
           emit(DataFavorite.Add)
       }
    }
}