package com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.library.add_playlist.fragment.AddNewPlayListFragment
import com.practicum.playlistmaker.ui.library.add_playlist.models.AddPlaylistState
import com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist.models.ContentEditPlaylist
import com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist.view_model.EditPlaylistViewModel
import com.practicum.playlistmaker.ui.share_data.SharedPlaylistViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditPlaylistFragment : AddNewPlayListFragment() {
    private var isInitialized = false

    private val sharedPlaylistViewModel by activityViewModel<SharedPlaylistViewModel>()
    private val viewModelEdit by viewModel<EditPlaylistViewModel>()

//    private var pic: Uri? = null

    private val binding
        get() = _binding!!



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isInitialized", isInitialized)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            isInitialized = savedInstanceState.getBoolean("isInitialized")
        }
        if (isInitialized) return
        isInitialized = true
        lifecycleScope.launch {
            sharedPlaylistViewModel.playlistFlow.collect {
                viewModelEdit.setPlaylist(it)
                super.viewModel.addImage(it.image!!)

            }
        }
    }


    override fun toolbarClickback() {
        findNavController().popBackStack()
    }

    override fun observeViewModel() {
        super.observeViewModel()

        lifecycleScope.launch {
            val event = viewModelEdit.channel.first()
            renderContentFirst(event)
        }

    }

    override fun btnAction() {
        val playlist = playlistCreate()
        viewModelEdit.createPlayList(playlist)
        findNavController().popBackStack()
    }

    private fun renderContentFirst(it: ContentEditPlaylist) {
        binding.library.text = getString(R.string.edit_playlist)
        binding.btnCreatePlaylist.text = getString(R.string.btn_save_playlist)

        binding.namePlaylist.setText(it.title)
        binding.descriptionPlaylist.setText(it.description)
    }


    override fun renderCreatePlaylist(it: AddPlaylistState.CreatePlayList) {
        findNavController().popBackStack()
    }

}