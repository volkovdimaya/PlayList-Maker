package com.practicum.playlistmaker.ui.library.info_playlist.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.info_playlist.models.PlaylistShare
import com.practicum.playlistmaker.domain.info_playlist.models.TrackPreview
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.info_playlist.api.UseCasePlaylistShare
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfo
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfoState
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    val dbInteractorPlaylist: DbInteractorPlaylist,
    val useCasePlaylistShare: UseCasePlaylistShare
) : ViewModel() {
//    private val _state: MutableLiveData<PlaylistInfoState> = MutableLiveData()
//    val state: LiveData<PlaylistInfoState> = _state

    private val _playlistItem = MutableSharedFlow<PlaylistInfoState>(replay = 1)
    val playlistItemFlow = _playlistItem.asSharedFlow()

    fun selectTrack(playlist: PlaylistItem) {
        Log.d("PlaylistInfoViewModel1", "selectTrack: $playlist")
        playlistItem = playlist
        viewModelScope.launch {
            combine(
                dbInteractorPlaylist.getPlaylistInfoById(playlist.id),
                dbInteractorPlaylist.getTrackInPlaylist(playlist.id)
            ) { playlistInfo, tracks ->
                Pair(playlistInfo, tracks)
            }.collect { (playlistInfo, tracks) ->
                Log.d("PlaylistInfoViewModel1", "selectTrack: $playlistInfo")
                playlistShare = playlistShare?.copy(
                    title = playlistInfo.title,
                    description = playlistInfo.description,
                    countTracks = playlistInfo.count,
                    tracks = tracks.map {
                        TrackPreview(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                        )
                    }
                ) ?: PlaylistShare(
                    title = playlistInfo.title,
                    description = playlistInfo.description,
                    countTracks = playlistInfo.count,
                    tracks = tracks.map {
                        TrackPreview(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                        )
                    }
                )
                Log.d("PlaylistInfoViewModel1", "playlistInfo: $playlistShare")
                _playlistItem.emit(PlaylistInfoState.Content(playlistInfo))
//                _state.value = PlaylistInfoState.Content(playlistInfo)
//                _state.value = PlaylistInfoState.ContentTracks(tracks)
            }

        }

    }

    private var playlistShare: PlaylistShare? = null
    private lateinit var playlistItem : PlaylistItem

//    fun setContent(playlistInfoState: PlaylistItem) {
//        playlistItem = playlistInfoState
//        viewModelScope.launch {
//            combine(
//                dbInteractorPlaylist.getPlaylistInfoById(playlistInfoState.id),
//                dbInteractorPlaylist.getTrackInPlaylist(playlistInfoState.id)
//            ) { playlistInfo, tracks ->
//                Pair(playlistInfo, tracks)
//            }.collect { (playlistInfo, tracks) ->
//
//                playlistShare = playlistShare?.copy(
//                    title = playlistInfo.title,
//                    description = playlistInfo.description,
//                    countTracks = playlistInfo.count,
//                    tracks = tracks.map {
//                        TrackPreview(
//                            trackName = it.trackName,
//                            artistName = it.artistName,
//                            trackTimeMillis = it.trackTimeMillis,
//                        )
//                    }
//                ) ?: PlaylistShare(
//                    title = playlistInfo.title,
//                    description = playlistInfo.description,
//                    countTracks = playlistInfo.count,
//                    tracks = tracks.map {
//                        TrackPreview(
//                            trackName = it.trackName,
//                            artistName = it.artistName,
//                            trackTimeMillis = it.trackTimeMillis,
//                        )
//                    }
//                )
//
//                _state.value = PlaylistInfoState.Content(playlistInfo)
//                _state.value = PlaylistInfoState.ContentTracks(tracks)
//            }
//
//        }
//
//    }

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

    fun onShareClick() {
        if (playlistShare?.tracks!!.isEmpty()) {
//            _state.value = PlaylistInfoState.EmptyPlaylist // TODO("Not yet implemented")
            return
        }
        playlistShare?.let{
            useCasePlaylistShare.sharePlaylist(playlistShare!!)
        }
    }


    companion object {
        const val CLICK_DEBOUNCE_DELAY = 2000L
    }

}