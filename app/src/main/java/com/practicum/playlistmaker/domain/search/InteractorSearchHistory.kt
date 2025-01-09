package com.practicum.playlistmaker.domain.interactor


import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.RepositorySearchHistory

class InteractorSearchHistory(private val repository: RepositorySearchHistory) {
    fun write(model: Track) {
        repository.update(model)
    }
    fun clear() {
        repository.clear()
    }
    fun getSong(): List<Track> {
        return repository.getSong()
    }
    fun hasHistory(): Boolean {
        return repository.hasHistory()
    }
}