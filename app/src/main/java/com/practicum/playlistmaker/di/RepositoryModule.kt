package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.repository.RepositorySearchHistoryImpl
import com.practicum.playlistmaker.data.repository.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.search.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.repository.ThemeRepository
import com.practicum.playlistmaker.domain.search.RepositorySearchHistory
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }


    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    single<RepositorySearchHistory> {
        RepositorySearchHistoryImpl(get(), get())//тут под вопросом передается объект
    }

}