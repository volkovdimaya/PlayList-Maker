package com.practicum.playlistmaker.data.playlistinfo.db

import androidx.room.Dao
import androidx.room.Query
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistInfoDao {

    @Query(
        """
        SELECT 
            p.*, 
            COUNT(pt.track_id) AS count, 
            SUM(pt.trackTimeMillis) AS duration 
        FROM playlist p
        LEFT JOIN playlist_and_track pat ON p.id = pat.playlist_id
        LEFT JOIN playlist_item pt ON pat.track_id = pt.track_id
        WHERE p.id = :id
        GROUP BY p.id
    """
    )
    fun getPlaylistInfoById(id: Long): Flow<PlaylistInfo>
}