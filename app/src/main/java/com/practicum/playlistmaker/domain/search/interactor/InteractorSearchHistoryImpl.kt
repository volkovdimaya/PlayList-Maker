package com.practicum.playlistmaker.domain.search.interactor


import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.InteractorSearchHistory
import com.practicum.playlistmaker.domain.search.RepositorySearchHistory

class InteractorSearchHistoryImpl(private val repository: RepositorySearchHistory) :
    InteractorSearchHistory {


    override fun write(model: Track) {
        repository.update(model)
    }


    override fun clear() {
        repository.clear()
    }

    override fun getSong(): List<Track> {
        return repository.getSong()
    }

    override fun hasHistory(): Boolean {
        return repository.hasHistory()
    }
}