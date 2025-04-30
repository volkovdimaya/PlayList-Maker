package com.practicum.playlistmaker.ui.share_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SharedPlaylistViewModel: ViewModel() {
    private val _playlistFlow = MutableSharedFlow<PlaylistItem>(replay = 1)
    val playlistFlow = _playlistFlow.asSharedFlow()


    fun selectPlaylist(playlist: PlaylistItem) {
        viewModelScope.launch {
            _playlistFlow.emit(playlist)
        }
    }
}