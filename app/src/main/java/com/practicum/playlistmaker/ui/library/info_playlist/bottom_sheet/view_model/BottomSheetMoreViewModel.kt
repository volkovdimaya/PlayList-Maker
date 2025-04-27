package com.practicum.playlistmaker.ui.library.info_playlist.bottom_sheet.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.ui.library.info_playlist.api.UseCasePlaylistShare
import com.practicum.playlistmaker.ui.library.info_playlist.models.UiEvent
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class BottomSheetMoreViewModel(
    val dbInteractorPlaylist: DbInteractorPlaylist,
    val useCasePlaylistShare: UseCasePlaylistShare
) : ViewModel() {

    private val _eventChannel = Channel<UiEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()


    private lateinit var playlistItem : PlaylistItem

    fun setPlaylist(playlist: PlaylistItem) {
        playlistItem = playlist
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            dbInteractorPlaylist.deletePlaylist(playlistItem.id)
        }
    }

    fun sharePlaylist() {
        viewModelScope.launch {
            if (!useCasePlaylistShare.sharePlaylist(playlistItem.id)){
                _eventChannel.send(UiEvent.TracksIsEmpty)
            }
        }
    }
}