package com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.practicum.playlistmaker.ui.library.add_playlist.models.AddPlaylistState
import com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist.models.ContentEditPlaylist
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.info_playlist.edit_playlist.view_model.EditPlaylistViewModel
import com.practicum.playlistmaker.ui.share_data.SharedPlaylistViewModel
import com.practicum.playlistmaker.util.showConfirmExitDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditPlaylistFragment : Fragment() {
    private var isInitialized = false

    private val sharedPlaylistViewModel by activityViewModel<SharedPlaylistViewModel>()
    private val viewModel by viewModel<EditPlaylistViewModel>()

    private var pic: Uri? = null

    private var _binding: FragmentEditPlaylistBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                viewModel.setPlaylist(it)

            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.namePlaylist.addTextChangedListener {
            viewModel.changeTitle(it.toString())
        }
        binding.btnImageAddPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.toolbar.setNavigationOnClickListener {
            viewModel.validationForm(playlistCreate())
        }
        binding.btnCreatePlaylist.setOnClickListener {
            val playlist = playlistCreate()
            viewModel.createPlayList(playlist)
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { it ->
            render(it)
        }

        lifecycleScope.launch {
            val event = viewModel.channel.first()
            renderContentFirst(event)
        }

    }

    private fun render(it: AddPlaylistState) {
        when (it) {
            is AddPlaylistState.Content -> {
                renderContent(it)
            }

            is AddPlaylistState.HasValidField -> {
                handleValidField(it)
            }

            is AddPlaylistState.CreatePlayList -> {
                renderCreatePlaylist(it)
            }
        }

    }

    private fun renderContent(it: AddPlaylistState.Content) {
        pic = it.uri
        binding.btnCreatePlaylist.isEnabled = it.btnEnabled

        if (it.uri != null) {
            binding.defaultImageAddPlaylist.visibility = View.GONE
            Glide
                .with(requireContext())
                .load(it.uri)
                .placeholder(R.drawable.place_holder_cover)
                .signature(ObjectKey(File(it.uri.path).lastModified()))
                .into(binding.imageAddPlaylist)
            binding.imageAddPlaylist.visibility = View.VISIBLE
        }
    }



    private fun renderContentFirst(it: ContentEditPlaylist) {
        binding.namePlaylist.setText(it.title)
        binding.descriptionPlaylist.setText(it.description)
    }

    private fun handleValidField(it: AddPlaylistState.HasValidField) {
        if (it.hasNotEmptyField) {
            showConfirmExitDialog {
                onBackPressedCallback.remove()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        } else {
            onBackPressedCallback.remove()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun renderCreatePlaylist(it: AddPlaylistState.CreatePlayList) {
            findNavController().popBackStack()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.validationForm(playlistCreate())
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.addImage(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private fun playlistCreate(): Playlist {
        return with(binding) {
            Playlist(
                title = namePlaylist.text.toString(),
                description = descriptionPlaylist.text.toString(),
                image = pic,
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
}