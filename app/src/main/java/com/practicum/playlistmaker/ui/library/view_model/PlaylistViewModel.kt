package com.practicum.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.ui.library.models.PlaylistState

class PlaylistViewModel() : ViewModel() {
    private val _state: MutableLiveData<PlaylistState> = MutableLiveData()
    val state: LiveData<PlaylistState> = _state

    init {
        _state.postValue(PlaylistState.PlaylistEmpty)
    }
}