package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface RepositorySearchHistory {
    fun update(model: Track)
    fun clear()
    fun getSong(): List<Track>
    fun hasHistory(): Boolean
}