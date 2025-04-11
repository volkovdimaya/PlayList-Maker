package com.practicum.playlistmaker.ui.library.add_playlist.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddNewPlaylistBinding
import com.practicum.playlistmaker.ui.library.add_playlist.models.AddPlaylistState
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.add_playlist.view_model.AddPlayListviewModel
import kotlinx.coroutines.launch
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
        Log.d("AddNewPlayListFragment", "render: $it")
        when (it) {
            AddPlaylistState.BtnEnabled -> {
                binding.btnCreatePlaylist.isEnabled = true
            }
            AddPlaylistState.BtnDisabled -> {
                binding.btnCreatePlaylist.isEnabled = false
            }
            is AddPlaylistState.ShowPic -> {
                renderPic(it)
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
            confirmDialog.show()
        }
    }

    private fun handleValidField(it: AddPlaylistState.HasValidField) {
        if (it.hasNotEmptyField) {
            confirmDialog.show()
        } else {
            onBackPressedCallback.remove()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun renderPic(it: AddPlaylistState.ShowPic) {
        binding.defaultImageAddPlaylist.visibility = View.GONE
        binding.imageAddPlaylist.setImageURI(it.uri)
        binding.imageAddPlaylist.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        _binding = null
    }

    private val confirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.title_complite_playlist))
            .setMessage(getString(R.string.message_data_lose))
            .setNeutralButton(getString(R.string.cancle)) { dialog, which -> }
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                onBackPressedCallback.remove()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.search_item_title)
                    )
                    getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.search_item_title)
                    )
                }
            }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.validationForm(playlistCreate())
        }
    }


    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            pic = uri
            if (uri != null) {
                viewModel.addImage(uri)


            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

}