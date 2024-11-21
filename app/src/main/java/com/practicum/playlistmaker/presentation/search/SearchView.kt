package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.TrackAudioPlayer

interface SearchView {
    fun showProgressBar()
    fun showNoInternet()
    fun showNoContent()
    fun showTracks()
    fun clickOnTrack(track : TrackAudioPlayer)

}