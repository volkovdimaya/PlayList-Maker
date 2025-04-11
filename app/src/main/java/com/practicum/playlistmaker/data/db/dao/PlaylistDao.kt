package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.practicum.playlistmaker.data.add_playlist.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.models.PlaylistWithTrackCount
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity) : Long

    @Query("""
        SELECT p.id, p.title, p.image, COUNT(t.track_id) as trackCount
        FROM playlist p
        LEFT JOIN playlist_and_track t ON p.id = t.playlist_id
        GROUP BY p.id
    """)
    fun getAllPlaylistsWithTrackCount(): Flow<List<PlaylistWithTrackCount>>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?
}