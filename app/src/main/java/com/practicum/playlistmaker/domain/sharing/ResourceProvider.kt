package com.practicum.playlistmaker.domain.sharing

interface ResourceProvider {
    fun getString(resId: Int): String
}