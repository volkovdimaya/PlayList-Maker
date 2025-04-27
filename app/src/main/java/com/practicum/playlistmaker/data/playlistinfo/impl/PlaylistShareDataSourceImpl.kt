package com.practicum.playlistmaker.data.playlistinfo.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.domain.info_playlist.api.PlaylistShareDataSource
import com.practicum.playlistmaker.data.playlistinfo.models.PlaylistShareDto
import com.practicum.playlistmaker.util.TimeFormatter
import com.practicum.playlistmaker.util.TrackCountFormatter

class PlaylistShareDataSourceImpl(private val context : Context): PlaylistShareDataSource {
    override fun sharePlaylist(playlist: PlaylistShareDto) {
        val shareText = buildString {
            append("${playlist.title}\n")
            append("${playlist.description}\n")
            append("${TrackCountFormatter.formatTrackCount(playlist.countTracks)} \n")
            playlist.tracks.forEachIndexed { index,  track ->
                append("${index+1}. ${track.artistName} - ${track.trackName} ${
                    TimeFormatter.formatDuration(
                        track.trackTimeMillis
                    )
                }\n")
            }
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
        }

        context.startActivity(sendIntent)
    }
}