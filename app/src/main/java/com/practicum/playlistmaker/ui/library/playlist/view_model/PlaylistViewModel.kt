package com.practicum.playlistmaker.ui.library.playlist.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.ui.library.playlist.mapper.toPlaylistItem
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactorPlaylist: DbInteractorPlaylist) : ViewModel() {
    private val _state: MutableLiveData<PlaylistState> = MutableLiveData()
    val state: LiveData<PlaylistState> = _state

    init {
        viewModelScope.launch {
            interactorPlaylist.getAllPlaylists().collect { playlists ->
                if (playlists.isEmpty()) {
                    _state.postValue(PlaylistState.PlaylistEmpty)
                } else {
                    _state.postValue(PlaylistState.PlaylistContent(playlists.map {
                        it.toPlaylistItem()
                    }))
                }
            }
        }
    }


}