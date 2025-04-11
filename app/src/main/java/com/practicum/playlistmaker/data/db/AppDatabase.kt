package com.practicum.playlistmaker.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.data.add_playlist.converter.UriConvertor
import com.practicum.playlistmaker.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.data.add_playlist.entity.PlaylistEntity
import com.practicum.playlistmaker.data.audioplayer.dao.PlaylistAndTrackDao
import com.practicum.playlistmaker.data.audioplayer.entity.PlayListAndTrackEntity
import com.practicum.playlistmaker.data.db.dao.TrackDao
import com.practicum.playlistmaker.data.db.entity.TrackEntity

@Database(
    version = 3, entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlayListAndTrackEntity::class
    ],
    autoMigrations = [
        AutoMigration (from = 2, to = 3)
    ]
)
@TypeConverters(UriConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistAndTrackDao(): PlaylistAndTrackDao




}

