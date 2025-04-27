package com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist.view_model


import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.add_playlist.use_case.ValidateImage
import com.practicum.playlistmaker.domain.add_playlist.use_case.ValidateTitle
import com.practicum.playlistmaker.ui.library.add_playlist.models.AddPlaylistState
import com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist.models.ContentEditPlaylist
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.add_playlist.view_model.AddPlayListviewModel
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val vallidateTitle: ValidateTitle,
    ValidateDescription: ValidateTitle,
    validateImage: ValidateImage,
    private val dbInteractorPlaylist: DbInteractorPlaylist
) : AddPlayListviewModel(vallidateTitle, ValidateDescription, validateImage, dbInteractorPlaylist) {

    private var playlistId: Long? = null


    private var _channel = Channel<ContentEditPlaylist>()
    val channel = _channel.receiveAsFlow()

    override fun createPlayList(playlist: Playlist) {
        val titleResult = vallidateTitle.execute(playlist.title)
        if (titleResult.successful) {
            viewModelScope.launch {
                dbInteractorPlaylist.updatePlaylist(playlist.copy(id = playlistId!!))
                _state.postValue(AddPlaylistState.CreatePlayList(true))
            }
        }

    }

    fun setPlaylist(playlist: PlaylistItem) {
        playlistId = playlist.id
        viewModelScope.launch {
            dbInteractorPlaylist.getPlaylistInfoById(playlist.id)
                .collect { result ->
                    val contentText = ContentEditPlaylist(
                        title = result!!.title,
                        description = result!!.description,
                    )
                    _content = AddPlaylistState.Content(
                        uri = result.image,
                        btnEnabled = true
                    )

                    _channel.send(contentText)
                    _state.postValue(content)
                }
        }
    }

}