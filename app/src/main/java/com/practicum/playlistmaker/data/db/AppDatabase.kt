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
import com.practicum.playlistmaker.data.audioplayer.entity.PlaylistItemEntity
import com.practicum.playlistmaker.data.db.dao.TrackDao
import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.data.playlistinfo.db.PlaylistInfoDao

@Database(
    version = 6, entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlayListAndTrackEntity::class,
        PlaylistItemEntity::class
    ],
//    autoMigrations = [
//        AutoMigration (from = 5, to = 6)
//    ]
)
@TypeConverters(UriConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistAndTrackDao(): PlaylistAndTrackDao
    abstract fun playlistInfoDao(): PlaylistInfoDao




}

val Migration_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE playlist_and_track_new (
                playlist_id INTEGER NOT NULL,
                track_id TEXT NOT NULL,
                PRIMARY KEY(playlist_id, track_id)
            )
        """.trimIndent())

        database.execSQL("""
            INSERT INTO playlist_and_track_new (playlist_id, track_id)
            SELECT playlist_id, track_id FROM playlist_and_track
        """.trimIndent())

        database.execSQL("DROP TABLE playlist_and_track")
        database.execSQL("ALTER TABLE playlist_and_track_new RENAME TO playlist_and_track")
    }
}


val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS playlist_item (
                trackTimeMillis INTEGER NOT NULL,
                artworkUrl100 TEXT NOT NULL,
                country TEXT NOT NULL,
                previewUrl TEXT NOT NULL,
                releaseDate TEXT NOT NULL,
                track_id TEXT NOT NULL PRIMARY KEY,
                artistName TEXT NOT NULL,
                trackName TEXT NOT NULL,
                primaryGenreName TEXT NOT NULL,
                collectionName TEXT NOT NULL
            )
        """.trimIndent())
    }
}

val Migration_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE playlist_and_track ADD COLUMN duration INTEGER NOT NULL DEFAULT 0")
  }
}
val migration4to5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE playlist_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL
            )
        """.trimIndent())

        database.execSQL("""
            INSERT INTO playlist_new (id, title, description)
            SELECT id, title, description FROM playlist
        """.trimIndent())

        // 3. Удаление старой таблицы
        database.execSQL("DROP TABLE playlist")

        database.execSQL("ALTER TABLE playlist_new RENAME TO playlist")
    }
}

