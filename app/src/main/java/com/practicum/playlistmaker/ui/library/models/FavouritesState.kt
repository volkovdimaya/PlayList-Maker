package com.practicum.playlistmaker.ui.library.models



sealed interface FavouritesState{
    data object FavouritesEmpty : FavouritesState
}