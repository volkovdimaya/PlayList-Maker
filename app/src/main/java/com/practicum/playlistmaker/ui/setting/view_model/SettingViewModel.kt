package com.practicum.playlistmaker.ui.setting.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingViewModel(
    private val themeInteractor: ThemeInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    init {
        val isDarkTheme = themeInteractor.isDarkTheme()
        _isDarkTheme.postValue(isDarkTheme)


    }

    fun onShareAppClicked() {
        sharingInteractor.shareApp()
    }

    fun onSendSupportClicked() {
        sharingInteractor.openSupport()
    }

    fun onUserAgreementClicked() {
        sharingInteractor.openTerms()
    }

    fun onThemeSwitched(isDarkTheme: Boolean) {
        themeInteractor.switchTheme(isDarkTheme)
        _isDarkTheme.value = isDarkTheme
    }

    companion object {

        fun provideFactory(
            themeInteractor: ThemeInteractor,
            sharingInteractor: SharingInteractor
        ) = viewModelFactory {
            initializer {
                SettingViewModel(themeInteractor, sharingInteractor)
            }
        }
    }
}