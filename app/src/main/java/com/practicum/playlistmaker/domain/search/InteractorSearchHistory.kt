package com.practicum.playlistmaker.domain.interactor


import com.practicum.playlistmaker.domain.search.RepositorySearchHistory

class InteractorSearchHistory(private val repository: RepositorySearchHistory) : RepositorySearchHistory by repository {

}