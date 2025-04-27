package com.practicum.playlistmaker.ui.audioplayer.view_model


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
    private val trackPlayer: TrackPlayer,
    private val InteractorFavorite: InteractorFavorite
) : ViewModel() {
    private val _screenStateLiveData =
        MutableLiveData<AudioPlayerScreenState>()

    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    private var timerJob: Job? = null

    private var _content: AudioPlayerScreenState.Content? = null
    private val content get() = _content!!

    private var track: Track? = null

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData


    fun clickFavorite() {

        viewModelScope.launch {
            InteractorFavorite.clickFavorite(track!!).collect { track ->
                _screenStateLiveData.value = when (track) {
                    DataFavorite.Add -> _content?.copy(isFavorite = true)
                    DataFavorite.Delete -> _content?.copy(isFavorite = false)
                }
            }
        }
    }


    fun play() {
        trackPlayer.play(track!!.previewUrl,
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

    fun loadContent(track : Track?) {
        this.track = track
        if (track == null) {
            _screenStateLiveData.postValue(
                AudioPlayerScreenState.Error
            )
        } else {
            _content = AudioPlayerScreenState.Content(track, false)

            _screenStateLiveData.postValue(content)
            viewModelScope.launch {
                InteractorFavorite.isFavorite(track).collect { isFavorite ->
                    _content = content.copy(
                        isFavorite = isFavorite
                    )
                    _screenStateLiveData.postValue(content)
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

    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
    }

}