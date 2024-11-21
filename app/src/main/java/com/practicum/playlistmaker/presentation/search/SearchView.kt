package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.models.Track

interface SearchView {
    fun showProgressBar()
    fun showNoInternet()
    fun showNoContent()
    fun showTracks()
    //fun showTracks(tracks: List<Track>)
}