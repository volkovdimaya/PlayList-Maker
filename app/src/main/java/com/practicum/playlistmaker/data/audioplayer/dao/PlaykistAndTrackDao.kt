package com.practicum.playlistmaker.data.audioplayer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.audioplayer.entity.PlayListAndTrackEntity
import com.practicum.playlistmaker.data.audioplayer.entity.PlaylistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistAndTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylistTablePlayListAndTrack(playlistAndTrackEntity: PlayListAndTrackEntity) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylistItem(playlist: PlaylistItemEntity)

    @Query("SELECT * FROM playlist_item WHERE track_id IN (SELECT track_id FROM playlist_and_track WHERE playlist_id = :id)")
    fun getTracksInPlaylist(id: Long): Flow<List<PlaylistItemEntity>>

    @Delete
    suspend fun deleteTrack(playlistItemEntity : PlaylistItemEntity)

    @Query("SELECT COUNT(*) FROM playlist_and_track WHERE track_id = :trackId")
    suspend fun getTrackPlaylistCount(trackId: String): Int

    @Delete
    suspend fun deletePlayListAndTrack(playlistAndTrackEntity: PlayListAndTrackEntity)


}