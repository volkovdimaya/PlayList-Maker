package com.practicum.playlistmaker.ui.share_data

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SharedTrackViewModel: ViewModel() {
    private val _trackFlow = MutableSharedFlow<Track>(replay = 1)
    val trackFlow = _trackFlow.asSharedFlow()


    suspend fun selectTrack(track: Track) {
        _trackFlow.emit(track)
    }
}