package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface InteractorSearchHistory {
    fun write(model: Track)
    fun clear()
    fun getSong(): List<Track>
    fun hasHistory(): Boolean
}