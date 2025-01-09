package com.practicum.playlistmaker.ui.search.models

import com.practicum.playlistmaker.domain.models.Track

sealed class SearchScreenState {
    object Loading: SearchScreenState()
    object NoContent: SearchScreenState()
    object NoInternet: SearchScreenState()
    data class Content(
        val trackModel: List<Track>,
    ): SearchScreenState()
}