package com.practicum.playlistmaker.domain.models

sealed interface Resource<T>
{
    data class Success<T>(val data : T) : Resource<T>
    data class Error<T>(val error : Int) : Resource<T>

}
