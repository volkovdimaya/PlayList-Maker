package com.practicum.playlistmaker.ui.library.add_playlist.api

import android.net.Uri

interface InteractorSavePic {
    fun savePic(uri: Uri, fileName : String)
    fun getPic(fileName: String): Uri?
}
