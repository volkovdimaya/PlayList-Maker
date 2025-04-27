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
import com.practicum.playlistmaker.ui.library.add_playlist.models.AddPlaylistState
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import kotlinx.coroutines.launch

open class AddPlayListviewModel(
    private val vallidateTitle: ValidateTitle,
    private val ValidateDescription: ValidateTitle,
    private val validateImage: ValidateImage,
    private val dbInteractorPlaylist: DbInteractorPlaylist

) : ViewModel() {
    protected val _state: MutableLiveData<AddPlaylistState> = MutableLiveData()

    val state: LiveData<AddPlaylistState> = _state

    protected var _content: AddPlaylistState.Content? = null
    val content: AddPlaylistState.Content
        get() = _content ?: AddPlaylistState.Content()

    init {
        _state.postValue(content)
    }

    fun changeTitle(title: String) {
        Log.d("renderContent", "changeTitle: ")
            if (title.isNotEmpty()) {
                _content = content.copy(btnEnabled = true)
            } else {
                _content = content.copy(btnEnabled = false)
            }
            _state.postValue(content)

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

    open fun createPlayList(playlist: Playlist) {
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
        _content = content.copy(uri = uri)
        _state.postValue(content)
    }


}