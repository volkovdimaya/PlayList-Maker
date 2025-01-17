package com.practicum.playlistmaker.ui.audioplayer.view_model



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.ui.audioplayer.models.PlayStatus
import com.practicum.playlistmaker.domain.player.models.TrackAudioPlayer
import com.practicum.playlistmaker.ui.audioplayer.models.AudioPlayerScreenState

class TrackViewModel(
    track: TrackAudioPlayer?,
    private val trackPlayer: TrackPlayer
) : ViewModel() {
    private val _screenStateLiveData =
        MutableLiveData<AudioPlayerScreenState>()

    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData


    fun play() {
        trackPlayer.play(
            statusObserver = object : TrackPlayer.StatusObserver {
                override fun onProgress(progress: Float) {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onStop() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                    pause()
                }

                override fun onPlay() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                }
            },
        )
    }

    init {
        if (track == null) {
            _screenStateLiveData.postValue(
                AudioPlayerScreenState.Error
            )
        } else {
            _screenStateLiveData.postValue(
                AudioPlayerScreenState.Content(track)
            )
        }
    }

    val screenStateLiveData: LiveData<AudioPlayerScreenState> = _screenStateLiveData

    fun pause() {
        trackPlayer.pause()
    }

    override fun onCleared() {
        trackPlayer.release()
    }

    companion object {
        fun getViewModelFactory(
            track: TrackAudioPlayer?,
            trackPlayer: TrackPlayer
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TrackViewModel(
                    track,
                    trackPlayer,
                )
            }
        }
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
    }
}