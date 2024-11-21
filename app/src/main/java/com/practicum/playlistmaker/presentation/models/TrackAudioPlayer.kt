package com.practicum.playlistmaker.presentation.models

import java.io.Serializable

data class TrackAudioPlayer(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long = 0,
    val artworkUrl100: String,
    val trackId: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val collectionName: String,
    val country: String,
    val previewUrl: String
) : Serializable