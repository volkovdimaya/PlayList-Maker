package com.practicum.playlistmaker.ui.search.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.search.interactor.InteractorSearchHistoryImpl
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.InteractorSearchHistory
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import com.practicum.playlistmaker.ui.search.models.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TrackSearchViewModel(
    private val interactorSearchHistoryImpl: InteractorSearchHistory,
    private val trackInteractor: TrackInteractorApi
) : ViewModel() {
    private var isClickAllowed = true

    private var textSearch: String = ""

    private var searchJob: Job? = null


    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.Empty)
    val searchState: StateFlow<SearchState> = _searchState

    private val _navigateToTrackDetails = SingleLiveEvent<Track>()
    val navigateToTrackDetails: LiveData<Track> = _navigateToTrackDetails

    fun updateRequest(query: String) {
        searchJob?.cancel()
        if (query.isEmpty()) {
            textSearch = ""
            _searchState.value = SearchState.BtnClear(false)
            updateHistory()

        } else {
            _searchState.value = SearchState.BtnClear(true)
            searchDebounce(query)
        }
    }

    init {
        updateHistory()
    }

    fun onSearchFocusGained() {
        if (interactorSearchHistoryImpl.hasHistory()) {
            updateHistory()
        }
    }


    fun clearSearch() {
        updateHistory()
    }


    private fun searchDebounce(changedText: String) {
        if (textSearch == changedText) {
            return
        }
        textSearch = changedText
        searchJob = viewModelScope.launch {
            delay(SEARCH_TRACK_DEBOUNCE_DELAY)
            loadTrack(textSearch)
        }
    }

    companion object {
        private const val SEARCH_TRACK_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 2000L


        fun provideFactory(
            interactorSearchHistoryImpl: InteractorSearchHistoryImpl,
            trackInteractor: TrackInteractorApi
        ) = viewModelFactory {
            initializer {
                TrackSearchViewModel(interactorSearchHistoryImpl, trackInteractor)
            }
        }
    }

    fun loadTrack(text: String) {
        textSearch = text
        _searchState.value = SearchState.ProgressBar

        viewModelScope.launch {
            trackInteractor.searchTracks(text).collect { data ->
                when (data) {
                    is DataConsumer.Success -> {
                        _searchState.value = SearchState.Content(data.data)
                    }

                    is DataConsumer.ResponseFailure -> {
                        _searchState.value = SearchState.NoInternet
                    }

                    is DataConsumer.ResponseNoContent -> {
                        _searchState.value = SearchState.NotContent
                    }
                }
            }
        }
    }

    private fun updateHistory() {
        val history = interactorSearchHistoryImpl.getSong().reversed()
        if (history.isNotEmpty()) {
            _searchState.value = SearchState.ContentHistory(history)
        } else {
            _searchState.value = SearchState.Empty
        }
    }

    fun clearHistory() {
        interactorSearchHistoryImpl.clear()
        _searchState.value = SearchState.Empty
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun onTrackClicked(track: Track) {
        if (clickDebounce()) {
            interactorSearchHistoryImpl.write(track)
            if (textSearch.isBlank()){
                updateHistory()
            }
            _navigateToTrackDetails.postValue(track)
        }
    }


}