package com.practicum.playlistmaker.data.audioplayer.mapper

import com.practicum.playlistmaker.data.audioplayer.entity.PlayListAndTrackEntity
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack

object mapPlaylistAndTrackToEntity {
    fun map(playListAndTrack: PlayListAndTrack): PlayListAndTrackEntity {
        return PlayListAndTrackEntity(
            playlistId = playListAndTrack.playlistId,
            trackId = playListAndTrack.trackId
        )
    }
}