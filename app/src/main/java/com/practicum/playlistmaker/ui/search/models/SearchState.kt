package com.practicum.playlistmaker.ui.search.models

import com.practicum.playlistmaker.domain.models.Track

sealed interface SearchState
{
    object ProgressBar : SearchState
    object NotContent : SearchState
    object Content : SearchState
    object NoInternet : SearchState
    object ContentHistory : SearchState
    object Empty : SearchState
}