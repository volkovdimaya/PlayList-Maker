package com.practicum.playlistmaker.data.audioplayer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.data.audioplayer.entity.PlayListAndTrackEntity

@Dao
interface PlaylistAndTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylistById(playlistAndTrackEntity: PlayListAndTrackEntity) : Long
}