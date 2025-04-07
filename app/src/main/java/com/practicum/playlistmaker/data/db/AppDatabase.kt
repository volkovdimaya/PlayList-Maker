package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
)
@TypeConverters(UriConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistAndTrackDao(): PlaylistAndTrackDao




}
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE track_table ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE track_table ADD COLUMN playlist_id INTEGER")

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS playlist (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                image TEXT
            )
        """.trimIndent())
    }
}
