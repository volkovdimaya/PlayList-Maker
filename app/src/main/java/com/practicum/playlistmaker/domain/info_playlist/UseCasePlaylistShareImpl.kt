package com.practicum.playlistmaker.domain.info_playlist


import com.practicum.playlistmaker.data.playlistinfo.models.PlaylistShareDto
import com.practicum.playlistmaker.data.playlistinfo.models.TrackPreviewDto
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.info_playlist.api.PlaylistShareDataSource
import com.practicum.playlistmaker.domain.info_playlist.models.PlaylistShare
import com.practicum.playlistmaker.domain.info_playlist.models.TrackPreview
import com.practicum.playlistmaker.ui.library.info_playlist.api.UseCasePlaylistShare
import kotlinx.coroutines.flow.first

class UseCasePlaylistShareImpl(
    private val playlistShareDataSource: PlaylistShareDataSource,
    val dbInteractorPlaylist: DbInteractorPlaylist
) :
    UseCasePlaylistShare {
    override suspend fun sharePlaylist(id: Long) : Boolean {
      val tracks =  dbInteractorPlaylist.getTrackInPlaylist(id).first()
        if (tracks.isEmpty()) {
            return false
        }
       val playlistInfo = dbInteractorPlaylist.getPlaylistInfoById(id).first()

        val playlistShare = PlaylistShare(
            title = playlistInfo!!.title,
            description = playlistInfo.description,
            countTracks = tracks.size,
            tracks = tracks.map { TrackPreview(it.trackName, it.artistName, it.trackTimeMillis) }
        )

        playlistShareDataSource.sharePlaylist(playlistShare.toDto())
        return true
    }
}

private fun PlaylistShare.toDto(): PlaylistShareDto {
    return PlaylistShareDto(
        title = title,
        description = description,
        countTracks = countTracks,
        tracks = tracks.map { it.toDto() }
    )
}

private fun TrackPreview.toDto(): TrackPreviewDto {
    return TrackPreviewDto(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
    )
}
