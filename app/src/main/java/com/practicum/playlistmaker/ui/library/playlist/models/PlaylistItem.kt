package com.practicum.playlistmaker.ui.library.playlist.models

import android.net.Uri
import android.os.Parcelable
import com.practicum.playlistmaker.RVItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistItem(
    val id : Long,
    val image : Uri?,
    val title : String,
    val trackCount : Int,
) : Parcelable, RVItem