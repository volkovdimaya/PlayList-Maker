package com.practicum.playlistmaker.ui.search.models

import com.practicum.playlistmaker.domain.models.Track

sealed interface SearchState
{
    data object ProgressBar : SearchState
    data object NotContent : SearchState
    data class Content(val tracks : List<Track>) : SearchState
    data object NoInternet : SearchState
    data class ContentHistory(val history: List<Track>) : SearchState
    data class BtnClear(val visible : Boolean) : SearchState
    data object Empty : SearchState
}