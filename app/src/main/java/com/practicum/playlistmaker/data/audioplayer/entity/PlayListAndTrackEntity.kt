package com.practicum.playlistmaker.data.audioplayer.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_and_track", primaryKeys = ["playlist_id", "track_id"])
data class PlayListAndTrackEntity(
    @ColumnInfo(name = "playlist_id")
    val playlistId : Long,
    @ColumnInfo(name = "track_id")
    val trackId : String
)