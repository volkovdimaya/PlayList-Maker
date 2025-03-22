package com.practicum.playlistmaker.ui.library.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.UseCaseGetFavoritesTracks
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.InteractorSearchHistory
import com.practicum.playlistmaker.ui.library.models.FavouritesState
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val useCaseGetFavoritesTracks: UseCaseGetFavoritesTracks,
) : ViewModel() {
    private val _state: MutableLiveData<FavouritesState> = MutableLiveData()
    val state: LiveData<FavouritesState> = _state

    private var isClickAllowed = true

    private val _navigateToTrackDetails = SingleLiveEvent<Track>()
    val navigateToTrackDetails: LiveData<Track> = _navigateToTrackDetails

    init {
        _state.postValue(FavouritesState.FavouritesEmpty)
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            useCaseGetFavoritesTracks
                .execute()
                .collect { tracks ->
                    render(tracks.reversed())
                }
        }

    }


    private fun render(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            _state.value = FavouritesState.FavouritesEmpty
        } else {
            _state.value = FavouritesState.Content(tracks)
        }
    }

    fun onTrackClicked(track: Track) {
        if (clickDebounce()) {
            _navigateToTrackDetails.postValue(track)
        }
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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 2000L
    }


}