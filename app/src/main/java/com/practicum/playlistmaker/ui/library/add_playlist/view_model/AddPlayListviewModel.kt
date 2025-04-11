package com.practicum.playlistmaker.ui.library.add_playlist.view_model


import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.add_playlist.use_case.ValidateImage
import com.practicum.playlistmaker.domain.add_playlist.use_case.ValidateTitle
import com.practicum.playlistmaker.ui.library.add_playlist.api.InteractorSavePic
import com.practicum.playlistmaker.ui.library.add_playlist.models.AddPlaylistState
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import kotlinx.coroutines.launch

class AddPlayListviewModel(
    private val vallidateTitle: ValidateTitle,
    private val ValidateDescription: ValidateTitle,
    private val validateImage: ValidateImage,
    private val dbInteractorPlaylist: DbInteractorPlaylist

) : ViewModel() {
    private val _state: MutableLiveData<AddPlaylistState> = MutableLiveData()

    val state: LiveData<AddPlaylistState> = _state

    init {
        _state.postValue(AddPlaylistState.BtnDisabled)
    }

    fun changeTitle(title: String) {
        if (title.isNotEmpty()) {
            _state.postValue(AddPlaylistState.BtnEnabled)
        } else {
            _state.postValue(AddPlaylistState.BtnDisabled)
        }
    }

    fun validationForm(playlist: Playlist) {

        val titleResult = vallidateTitle.execute(playlist.title)
        val desResult = ValidateDescription.execute(playlist.description)
        val imageResult = validateImage.execute(playlist.image)

        val hasValidField = listOf(
            titleResult,
            desResult,
            imageResult
        ).any { it.successful }

        if (hasValidField) {
            _state.postValue(AddPlaylistState.HasValidField(true))
        } else {
            _state.postValue(AddPlaylistState.HasValidField(false))
        }
    }

    fun createPlayList(playlist: Playlist) {
        val titleResult = vallidateTitle.execute(playlist.title)
        if (titleResult.successful) {
            viewModelScope.launch {
                dbInteractorPlaylist
                    .insertPlaylist(playlist)
                    .collect { result ->
                        _state.postValue(AddPlaylistState.CreatePlayList(result))
                    }

            }
        } else {
            _state.postValue(AddPlaylistState.CreatePlayList(false))
        }


    }

    fun addImage(uri: Uri) {
        _state.value = AddPlaylistState.ShowPic(uri)
    }


}