package com.practicum.playlistmaker.data.search.dto

data class TrackDto (
    val trackName: String,
     val artistName: String ,
     val trackTimeMillis: Long  = 0,
     val artworkUrl100: String,
     val trackId: String,
     val releaseDate: String,
     val primaryGenreName: String,
     val collectionName: String,
     val country: String,
     val previewUrl: String
                     )