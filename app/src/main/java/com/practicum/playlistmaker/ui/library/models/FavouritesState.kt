package com.practicum.playlistmaker.ui.library.models

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.TrackAudioPlayer


sealed interface FavouritesState{
    data object FavouritesEmpty : FavouritesState

    data class Content(val tracks : List<Track>) : FavouritesState
}