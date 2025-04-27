package com.practicum.playlistmaker.data.playlistinfo.models



data class PlaylistShareDto(
    val title : String,
    val description : String,
    val countTracks : Int,
    val tracks : List<TrackPreviewDto>,
)