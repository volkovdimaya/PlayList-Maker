package com.practicum.playlistmaker.presentation.search


import com.practicum.playlistmaker.presentation.models.TrackAudioPlayer
import com.practicum.playlistmaker.ui.search.models.SearchState

interface SearchView {
    //fun showProgressBar()
    //fun showNoInternet()
    //fun showNoContent()
    //fun showTracks()
    fun clickOnTrack(track : TrackAudioPlayer)

    fun render(state: SearchState)

}