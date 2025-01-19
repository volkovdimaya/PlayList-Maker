package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.ResourceProvider
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return resourceProvider.getString(R.string.link_course)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            resourceProvider.getString(R.string.email_developer),
            resourceProvider.getString(R.string.send_developer_body),
            resourceProvider.getString(R.string.send_developer_theme)
        )
    }

    private fun getTermsLink(): String {
        return resourceProvider.getString(R.string.link_user_agreement)

    }
}