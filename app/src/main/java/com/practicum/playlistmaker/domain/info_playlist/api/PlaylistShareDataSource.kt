package com.practicum.playlistmaker.domain.info_playlist.api

import com.practicum.playlistmaker.data.playlistinfo.models.PlaylistShareDto

interface PlaylistShareDataSource {
    fun sharePlaylist(playlist: PlaylistShareDto)
}