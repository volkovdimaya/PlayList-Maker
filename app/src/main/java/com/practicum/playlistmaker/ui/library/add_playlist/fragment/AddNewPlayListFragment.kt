package com.practicum.playlistmaker.ui.library.add_playlist.fragment


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
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.databinding.FragmentAddNewPlaylistBinding
import com.practicum.playlistmaker.ui.library.add_playlist.models.AddPlaylistState
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.add_playlist.view_model.AddPlayListviewModel
import com.practicum.playlistmaker.util.showConfirmExitDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddNewPlayListFragment : Fragment() {

    private var _binding: FragmentAddNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AddPlayListviewModel>()
    private var pic: Uri? = null

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        binding.namePlaylist.addTextChangedListener {
            viewModel.changeTitle(it.toString())
        }

        binding.btnImageAddPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnCreatePlaylist.setOnClickListener {
            val playlist = playlistCreate()
            viewModel.createPlayList(playlist)
        }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.validationForm(playlistCreate())
        }

        observeViewModel()
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

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
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

    private fun renderCreatePlaylist(it: AddPlaylistState.CreatePlayList) {
        if (it.successfully) {
            Snackbar.make(
                binding.root,
                "Плейлист ${playlistCreate().title} создан",
                Snackbar.LENGTH_LONG
            ).show()
            findNavController().popBackStack()
        } else {
            binding.btnCreatePlaylist.isEnabled = false
            showConfirmExitDialog {
                onBackPressedCallback.remove()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
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

    private fun renderContent(it: AddPlaylistState.Content) {
        pic = it.uri
        binding.btnCreatePlaylist.isEnabled = it.btnEnabled

        if (it.uri != null) {
            binding.defaultImageAddPlaylist.visibility = View.GONE
            binding.imageAddPlaylist.setImageURI(it.uri)
            binding.imageAddPlaylist.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()

        //        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

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

}