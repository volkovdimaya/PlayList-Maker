package com.practicum.playlistmaker.data.dto



data class SearchTrackRequest (val term: String,
                               val entity: String = "song"
)