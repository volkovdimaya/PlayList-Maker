package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.mapper.tiPlaylistBottomSheetItem
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.BottomSheetClickState
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.BottomSheetState
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.launch

class BottomSheetViewModel(private val interactorPlaylist: DbInteractorPlaylist) : ViewModel() {

    private val _state = MutableLiveData<BottomSheetState>()
    val state: LiveData<BottomSheetState>
        get() = _state

    private val  _state_click = SingleLiveEvent<BottomSheetClickState>()
    val state_click: LiveData<BottomSheetClickState>
        get() = _state_click

    fun addTrackToPlaylist(track : Track, playListAndTrack: PlayListAndTrack, title: String) {

        viewModelScope.launch {
            interactorPlaylist.addTrackToPlaylist(track, playListAndTrack).collect { result ->
                if (result) {
                    _state_click.value = BottomSheetClickState.TrackAddPlaylist(title)
                } else {
                    _state_click.value = BottomSheetClickState.TrackNotAddPlaylist(title)
                }

            }
        }
    }



    init {
        viewModelScope.launch {
            interactorPlaylist.getAllPlaylists().collect { playlists ->
                if (playlists.isEmpty()) {
                    _state.postValue(BottomSheetState.idle)
                } else {
                    _state.postValue(BottomSheetState.Content(playlists.map { it.tiPlaylistBottomSheetItem() }))
                }
            }
        }
    }


}