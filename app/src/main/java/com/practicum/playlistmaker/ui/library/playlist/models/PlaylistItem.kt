package com.practicum.playlistmaker.ui.library.playlist.models

import android.net.Uri
import com.practicum.playlistmaker.RVItem

data class PlaylistItem(
    val image : Uri?,
    val title : String,
    val trackCount : Int,
) : RVItem