package com.practicum.playlistmaker.domain.add_playlist

import android.net.Uri

interface PicRepository {
    fun savePic(uri : Uri, fileName: String)
    fun getPic(fileName: String): Uri?
    fun deletePic(fileName: String)
}
