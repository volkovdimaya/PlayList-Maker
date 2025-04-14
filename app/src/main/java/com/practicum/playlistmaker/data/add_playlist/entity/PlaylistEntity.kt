package com.practicum.playlistmaker.data.add_playlist.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.practicum.playlistmaker.data.add_playlist.converter.UriConvertor

@Entity(tableName = "playlist")
@TypeConverters(UriConvertor::class)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "image")
    val image: Uri? = null
)