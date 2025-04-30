package com.practicum.playlistmaker.ui.library.info_playlist.models

import com.practicum.playlistmaker.domain.models.Track

sealed class PlaylistInfoState{
    data class Content(val playlistInfo: PlaylistInfo, val tracks: List<Track>) : PlaylistInfoState()
}
