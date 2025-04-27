package com.practicum.playlistmaker.domain.info_playlist.models

data class PlaylistShare(
    val title : String,
    val description : String,
    val countTracks : Int,
    val tracks : List<TrackPreview>,
)
