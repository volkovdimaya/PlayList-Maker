package com.practicum.playlistmaker.ui.audioplayer.view_model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.InteractorFavorite
import com.practicum.playlistmaker.domain.db.model.DataFavorite
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.ui.audioplayer.models.PlayStatus
import com.practicum.playlistmaker.ui.audioplayer.models.AudioPlayerScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackViewModel(
    private val track: Track?,
    private val trackPlayer: TrackPlayer,
    private val InteractorFavorite: InteractorFavorite
) : ViewModel() {
    private val _screenStateLiveData =
        MutableLiveData<AudioPlayerScreenState>()

    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    private var timerJob: Job? = null

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    fun clickFavorite() {
        viewModelScope.launch {
            InteractorFavorite.clickFavorite(track!!).collect { track ->
                _screenStateLiveData.value = when (track) {
                    DataFavorite.Add -> AudioPlayerScreenState.IsFavorite(true)
                    DataFavorite.Delete -> AudioPlayerScreenState.IsFavorite(false)
                }
            }
        }
    }


    fun play() {
        trackPlayer.play(
            statusObserver = object : TrackPlayer.StatusObserver {
                override fun onStop() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                    pause()
                }

                override fun onPlay() {
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                    startTimer()
                }

                override fun onCompletion() {
                    pause()
                    playStatusLiveData.value =
                        getCurrentPlayStatus().copy(isPlaying = false, progress = 0.0f)
                }
            },
        )
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(SECOND)
                playStatusLiveData.value =
                    getCurrentPlayStatus().copy(progress = trackPlayer.getProgress())
            }

        }
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
            viewModelScope.launch {
                InteractorFavorite.isFavorite(track).collect { isFavorite ->
                    _screenStateLiveData.postValue(
                        AudioPlayerScreenState.IsFavorite(isFavorite)
                    )
                }
            }
        }
    }

    val screenStateLiveData: LiveData<AudioPlayerScreenState> = _screenStateLiveData

    fun pause() {
        timerJob?.cancel()
        trackPlayer.pause()
    }


    override fun onCleared() {
        trackPlayer.release()
    }

    companion object {
        private const val SECOND = 300L

//        fun getViewModelFactory(
//            track: TrackAudioPlayer?,
//            trackPlayer: TrackPlayer
//        ): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                TrackViewModel(
//                    track,
//                    trackPlayer,
//                )
//            }
//        }
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
    }

}