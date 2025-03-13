package com.practicum.playlistmaker.ui.search.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import com.practicum.playlistmaker.ui.search.models.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TrackSearchViewModel(
    private val interactorSearchHistory: InteractorSearchHistory,
    private val trackInteractor: TrackInteractorApi
) : ViewModel() {
    private var isClickAllowed = true

    private var textSearch: String = ""

    private var searchJob: Job? = null


    private val _searchState: MutableLiveData<SearchState> = MutableLiveData()
    val searchState: LiveData<SearchState> = _searchState

    private val _navigateToTrackDetails = SingleLiveEvent<Track>()
    val navigateToTrackDetails: LiveData<Track> = _navigateToTrackDetails

    fun updateRequest(query: String) {
        searchJob?.cancel()
        if (query.isEmpty()) {
            textSearch = ""
            _searchState.value = SearchState.BtnClear(false)
            updateHistory()

        } else {
            _searchState.postValue(SearchState.BtnClear(true))
            searchDebounce(query)
        }
    }

    init {
        updateHistory()
    }

    fun onSearchFocusGained() {
        if (interactorSearchHistory.hasHistory()) {
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
            interactorSearchHistory: InteractorSearchHistory,
            trackInteractor: TrackInteractorApi
        ) = viewModelFactory {
            initializer {
                TrackSearchViewModel(interactorSearchHistory, trackInteractor)
            }
        }
    }

    fun loadTrack(text: String) {
        textSearch = text
        _searchState.postValue(SearchState.ProgressBar)

        viewModelScope.launch {
            trackInteractor.searchTracks(text).collect { data ->
                when (data) {
                    is DataConsumer.Success -> {
                        _searchState.postValue(SearchState.Content(data.data))
                    }

                    is DataConsumer.ResponseFailure -> {
                        _searchState.postValue(SearchState.NoInternet)
                    }

                    is DataConsumer.ResponseNoContent -> {
                        _searchState.postValue(SearchState.NotContent)
                    }
                }
            }
        }
    }

    private fun updateHistory() {
        val history = interactorSearchHistory.getSong().reversed()
        if (history.isNotEmpty()) {
            _searchState.postValue(SearchState.ContentHistory(history))
        } else {
            _searchState.postValue(SearchState.Empty)
        }
    }

    fun clearHistory() {
        interactorSearchHistory.clear()
        _searchState.postValue(SearchState.Empty)
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
            interactorSearchHistory.update(track)
            if (textSearch.isBlank()){
                updateHistory()
            }
            _navigateToTrackDetails.postValue(track)
        }
    }


}