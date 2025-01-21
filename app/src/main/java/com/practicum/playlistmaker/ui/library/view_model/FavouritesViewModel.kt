package com.practicum.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.ui.library.models.FavouritesState

class FavouritesViewModel(): ViewModel() {
    private val _state: MutableLiveData<FavouritesState> = MutableLiveData()
    val state: LiveData<FavouritesState> = _state

    init {
        _state.postValue(FavouritesState.FavouritesEmpty)
    }
}