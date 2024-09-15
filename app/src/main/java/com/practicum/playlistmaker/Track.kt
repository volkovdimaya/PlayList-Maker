package com.practicum.playlistmaker

data class Track(val trackName: String,
            val artistName: String ,
            val trackTimeMillis: Long  = 0,
            val artworkUrl100: String )