package com.practicum.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import com.practicum.playlistmaker.ui.search.models.SearchState


class TrackSearchViewModel(
    private val interactorSearchHistory: InteractorSearchHistory, // История поиска
    private val trackInteractor: TrackInteractorApi
) : ViewModel() {
    private var isClickAllowed = true

    private var textSearch: String = ""

    private val handler = Handler(Looper.getMainLooper())


    private val _searchState: MutableLiveData<SearchState> = MutableLiveData()
    val searchState: LiveData<SearchState> = _searchState

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks


    private val _historyTracks = MutableLiveData<List<Track>>()
    val historyTracks: LiveData<List<Track>> = _historyTracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToTrackDetails = SingleLiveEvent<Track>()
    val navigateToTrackDetails: LiveData<Track> = _navigateToTrackDetails

    private val _showBtnClear = MutableLiveData<Boolean>()
    val showBtnClear: LiveData<Boolean> = _showBtnClear


    fun updateRequest(query: String) {

        if (query.isEmpty()) {
            handler.removeCallbacks(searchRunnable)
            updateHistory()
            //_searchState.postValue(SearchState.ContentHistory)
            _showBtnClear.value =false

        } else {
            _showBtnClear.postValue(true)
            searchDebounce(query)
        }
    }
    init {
        updateHistory()
    }

    override fun onCleared() {
        handler.removeCallbacks(searchRunnable)
    }

    fun onSearchFocusGained() {
        Log.d("12121", "onSearchFocusGained")
        //updateHistory()
        if (interactorSearchHistory.hasHistory()){
            _searchState.postValue(SearchState.ContentHistory)
        }
    }


    fun clearSearch() {
        updateHistory()
        //_searchState.postValue(SearchState.ContentHistory)
    }

    private var searchRunnable = Runnable {
        loadTrack(textSearch)
    }

    private fun searchDebounce(changedText: String) {
        if (textSearch == changedText) {
            return
        }
        textSearch = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_TRACK_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_TRACK_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 2000L
        const val FIELD_SEARCH = "FIELD_SEARCH"

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

        handler.removeCallbacks(searchRunnable)

        trackInteractor.searchTracks(text,
            object : TrackConsumer<List<Track>> {
                override fun consume(data: DataConsumer<List<Track>>) {
                    when (data) {
                        is DataConsumer.Success -> {
                                _tracks.postValue(data.data)
                                _searchState.postValue(SearchState.Content)
                        }

                        is DataConsumer.ResponseFailure -> {
                                _searchState.postValue(SearchState.NoInternet)
                        }

                        is DataConsumer.ResponseNoContent -> {
                            _searchState.postValue(SearchState.NotContent)
                        }
                    }
                }

            })
    }

    private fun updateHistory() {
        val history = interactorSearchHistory.getSong().reversed()
        _historyTracks.postValue(history)
        if (history.isNotEmpty()){
            _searchState.postValue(SearchState.ContentHistory)
        }
    }

    fun clearHistory() {
        interactorSearchHistory.clear()
        updateHistory()
        _searchState.postValue(SearchState.Empty)
    }

    private fun clickDebonce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    fun onTrackClicked(track: Track) {
        if (clickDebonce()) {
            interactorSearchHistory.write(track)
            //updateHistory()
            _navigateToTrackDetails.postValue(track)
        }
    }


}