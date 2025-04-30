package com.practicum.playlistmaker.domain.add_playlist.mapper

import com.practicum.playlistmaker.data.add_playlist.entity.PlaylistEntity
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist

object PlaylistMapper {
    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            title = playlist.title,
            description = playlist.description,
        )
    }

    fun mapToDomain(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            title = playlistEntity.title,
            description = playlistEntity.description,
        )
    }
}