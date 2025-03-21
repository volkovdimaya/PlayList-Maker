package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.TrackDao
import com.practicum.playlistmaker.data.db.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun trackDao() : TrackDao
}