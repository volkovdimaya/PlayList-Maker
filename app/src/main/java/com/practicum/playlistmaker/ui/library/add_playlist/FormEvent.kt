package com.practicum.playlistmaker.ui.library.add_playlist

sealed class FormEvent {
    data class TitleChanged(val email : String) : FormEvent()
    data class DescriptionChanged(val name : String) : FormEvent()

    data object Submit : FormEvent()
}