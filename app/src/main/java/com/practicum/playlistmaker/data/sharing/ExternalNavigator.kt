package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(link : String)
    fun openLink(link : String)
    fun openEmail(data : EmailData)
}