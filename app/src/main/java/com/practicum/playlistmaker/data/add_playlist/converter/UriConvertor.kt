package com.practicum.playlistmaker.data.add_playlist.converter

import android.net.Uri
import androidx.room.TypeConverter

class UriConvertor {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}