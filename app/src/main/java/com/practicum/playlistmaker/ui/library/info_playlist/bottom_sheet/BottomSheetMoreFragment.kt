package com.practicum.playlistmaker.ui.library.info_playlist.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.BottomSheetPlaylistMoreBinding
import com.practicum.playlistmaker.ui.library.info_playlist.bottom_sheet.view_model.BottomSheetMoreViewModel
import com.practicum.playlistmaker.ui.library.info_playlist.models.UiEvent
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import com.practicum.playlistmaker.ui.share_data.SharedPlaylistViewModel
import com.practicum.playlistmaker.util.TrackCountFormatter
import com.practicum.playlistmaker.util.showConfirmDeleteDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class BottomSheetMoreFragment : BottomSheetDialogFragment() {

    private val viewModel  by viewModel<BottomSheetMoreViewModel>()
    private val sharedPlaylistViewModel by activityViewModel<SharedPlaylistViewModel>()

    private var _binding: BottomSheetPlaylistMoreBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPlaylistMoreBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            launch {
                sharedPlaylistViewModel.playlistFlow.collect{ playlistItem ->
                    renderInfoPlaylist(playlistItem)
                    viewModel.setPlaylist(playlistItem)
                }
            }

            launch {
                viewModel.eventChannel.collect { event ->
                    when (event) {
                        UiEvent.TracksIsEmpty -> renderSnackbarEmptyTracks()
                    }
                }
            }

        }



        binding.sharePlaylist.setOnClickListener {
            viewModel.sharePlaylist()
        }
        binding.editInfo.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.action_playlistInfo_to_editPlaylistFragment)
        }
        binding.deletePlaylist.setOnClickListener {
            showConfirmDeleteDialog(
                onConfirm = {
                    lifecycleScope.launch {
                        viewModel.deletePlaylist()
                        findNavController().popBackStack()
                        dismiss()
                    }
                },
                message = getString(R.string.dialog_delete_playlist_message, "${binding.title.text}"),
                title = getString(R.string.dialog_delete_playlist),
            )
        }

    }

    private fun renderInfoPlaylist(playlistItem: PlaylistItem) {
        Glide
            .with(requireContext())
            .load(playlistItem.image)
            .placeholder(R.drawable.place_holder_cover)
            .signature(ObjectKey(File(playlistItem.image!!.path).lastModified()))
            .into(binding.image)
        binding.title.text = playlistItem.title
        binding.count.text = TrackCountFormatter.formatTrackCount(playlistItem.trackCount)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderSnackbarEmptyTracks() {
        Snackbar.make(
            requireView(),
            getString(R.string.empty_playlist),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}