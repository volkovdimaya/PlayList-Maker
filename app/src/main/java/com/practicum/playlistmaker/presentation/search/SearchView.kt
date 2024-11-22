package com.practicum.playlistmaker.presentation.search


import com.practicum.playlistmaker.presentation.models.TrackAudioPlayer
import com.practicum.playlistmaker.ui.search.models.SearchState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SearchView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clickOnTrack(track : TrackAudioPlayer)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: SearchState)

}