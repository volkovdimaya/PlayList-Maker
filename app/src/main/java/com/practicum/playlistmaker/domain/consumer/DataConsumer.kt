package com.practicum.playlistmaker.domain.consumer


sealed interface DataConsumer<T> {
    data class Success<T>(val data: T) : DataConsumer<T>
    class ResponseFailure<T> : DataConsumer<T>
    class ResponseNoContent<T> : DataConsumer<T>
}
