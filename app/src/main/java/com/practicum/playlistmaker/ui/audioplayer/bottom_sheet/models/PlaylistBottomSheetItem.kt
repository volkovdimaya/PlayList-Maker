package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models

import android.net.Uri
import com.practicum.playlistmaker.RVItem

data class PlaylistBottomSheetItem(
    val id: Long,
    val image: Uri?,
    val title: String,
    val trackCount: Int,
) : RVItem
