package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.ui.search.SingleLiveEvent

class AudioPlayerEventFromBottomSheet : ViewModel() {
    private val _event = SingleLiveEvent<String>()
    val event : LiveData<String>
        get() = _event

    fun eventTrackAddPlaylist(title: String) {
        _event.value = title
    }
}