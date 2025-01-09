package com.practicum.playlistmaker.data.sharing.impl

import android.content.Context
import com.practicum.playlistmaker.domain.sharing.ResourceProvider

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}