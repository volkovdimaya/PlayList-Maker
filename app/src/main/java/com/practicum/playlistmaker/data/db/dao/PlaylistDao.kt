package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.data.add_playlist.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCountDataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity) : Long

    @Query("""
        SELECT p.id, p.title, COUNT(pi.track_id) as trackCount
        FROM playlist p
        LEFT JOIN playlist_and_track pat ON p.id = pat.playlist_id
        LEFT JOIN playlist_item pi ON pat.track_id = pi.track_id
        GROUP BY p.id
    """)
    fun getAllPlaylistsWithTrackCount(): Flow<List<PlaylistWithTrackCountDataSource>>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Query("UPDATE playlist SET title = :title, description = :description WHERE id = :id")
    suspend fun updatePlaylist(id: Long, title: String, description: String): Int

    @Query("DELETE FROM playlist WHERE id = :id")
    suspend fun deletePlaylist(id: Long): Int

}