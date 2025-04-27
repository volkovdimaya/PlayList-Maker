package com.practicum.playlistmaker.ui.library.info_playlist.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.info_playlist.api.UseCasePlaylistShare
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfoState
import com.practicum.playlistmaker.ui.library.info_playlist.models.UiEvent
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    val dbInteractorPlaylist: DbInteractorPlaylist,
    val useCasePlaylistShare: UseCasePlaylistShare
) : ViewModel() {

    private val _eventChannel = Channel<UiEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _playlistItem = MutableLiveData<PlaylistInfoState>()
    val playlistItemFlow : LiveData<PlaylistInfoState> = _playlistItem

    fun selectPlaylist(playlist: PlaylistItem) {
        playlistItem = playlist
        viewModelScope.launch {
            combine(
                dbInteractorPlaylist.getPlaylistInfoById(playlist.id),
                dbInteractorPlaylist.getTrackInPlaylist(playlist.id)
            ) { playlistInfo, tracks ->
                Pair(playlistInfo, tracks)
            }.collect { (playlistInfo, tracks) ->
                if (playlistInfo != null)
                    _playlistItem.postValue(PlaylistInfoState.Content(playlistInfo, tracks))
            }

        }

    }

    private lateinit var playlistItem : PlaylistItem



    fun onDeleteTrackClick(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            val playListAndTrack = PlayListAndTrack(
                playlistId = playlistItem.id,
                trackId = track.trackId
            )
            dbInteractorPlaylist.deleteTrack(track, playListAndTrack)
        }
    }


    private val _navigateToTrackDetails = SingleLiveEvent<Track>()
    val navigateToTrackDetails: LiveData<Track> = _navigateToTrackDetails

    fun onTrackClicked(track: Track) {
        if (clickDebounce()) {
            _navigateToTrackDetails.postValue(track)
        }
    }

    private var isClickAllowed = true

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

    suspend fun onShareClick() {
        if (!useCasePlaylistShare.sharePlaylist(playlistItem.id)){
            _eventChannel.send(UiEvent.TracksIsEmpty)
        }
    }


    companion object {
        const val CLICK_DEBOUNCE_DELAY = 2000L
    }

}