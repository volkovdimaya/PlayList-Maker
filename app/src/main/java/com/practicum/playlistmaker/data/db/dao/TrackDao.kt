package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track : TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun delete(track : TrackEntity)

    @Query("SELECT * FROM track_table ")
    suspend fun getTracksFavorites() : List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE track_id = :trackId")
    suspend fun getTrackFavorite(trackId : String) : TrackEntity?


}