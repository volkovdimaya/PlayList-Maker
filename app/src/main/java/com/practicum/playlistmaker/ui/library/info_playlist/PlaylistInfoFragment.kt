package com.practicum.playlistmaker.ui.library.info_playlist


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.practicum.playlistmaker.domain.models.Track

import com.practicum.playlistmaker.ui.library.info_playlist.adapter.TrackInPlaylistAdapter
import com.practicum.playlistmaker.ui.library.info_playlist.bottom_sheet.BottomSheetMoreFragment
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfoState
import com.practicum.playlistmaker.ui.library.info_playlist.models.UiEvent
import com.practicum.playlistmaker.ui.library.info_playlist.view_model.PlaylistInfoViewModel
import com.practicum.playlistmaker.ui.share_data.SharedPlaylistViewModel
import com.practicum.playlistmaker.ui.share_data.SharedTrackViewModel
import com.practicum.playlistmaker.util.TimeFormatter
import com.practicum.playlistmaker.util.TrackCountFormatter
import com.practicum.playlistmaker.util.showConfirmDeleteDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File


class PlaylistInfoFragment : Fragment() {
    var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")

    private val viewModel: PlaylistInfoViewModel by activityViewModel()
    private val sharedTrackViewModel: SharedTrackViewModel by activityViewModel()
    private val sharedPlaylistViewModel by activityViewModel<SharedPlaylistViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTracks.layoutManager = LinearLayoutManager(requireContext())
        val trakAdapter = TrackInPlaylistAdapter(
            emptyList(),
            object : TrackInPlaylistAdapter.OnItemClickListener {

                override fun onItemClick(track: Track) {
                    viewModel.onTrackClicked(track)
                }

                override fun onItemLongClick(track: Track): Boolean {
                    showConfirmDeleteDialog(
                        onConfirm = {
                            viewModel.onDeleteTrackClick(track)
                        },
                        message = getString(R.string.delete_track),
                        title = getString(R.string.title_delete_track_playlist),
                    )
                    return false
                }
            })
        binding.recyclerTracks.adapter = trakAdapter



        binding.toolbarPlaylistInfo.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.playlistItemFlow.observe(viewLifecycleOwner) { playlistItem ->
            when (playlistItem) {
                is PlaylistInfoState.Content -> renderContent(playlistItem)
            }
        }

        lifecycleScope.launch {
            launch {
                sharedPlaylistViewModel.playlistFlow.collect { playlist ->
                    viewModel.selectPlaylist(playlist)
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




        binding.more.setOnClickListener {
            BottomSheetMoreFragment().show(parentFragmentManager, "MyBottomSheetMore")
        }
        binding.share.setOnClickListener {
            lifecycleScope.launch {
                viewModel.onShareClick()
            }

        }



        viewModel.navigateToTrackDetails.observe(viewLifecycleOwner) { track ->
            lifecycleScope.launch {
                sharedTrackViewModel.selectTrack(track)
            }
            findNavController().navigate(R.id.action_playlistInfo_to_audioPlayerFragment)
        }
    }

    private fun renderSnackbarEmptyTracks() {
        Snackbar.make(
            requireView(),
            getString(R.string.empty_playlist),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun renderContentTracks(tracks: List<Track>) {
        if (tracks.isEmpty()){
            Snackbar.make(
                requireView(),
                getString(R.string.message_empty_playlist),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        (binding.recyclerTracks.adapter as TrackInPlaylistAdapter).updateData(tracks)
    }

    private fun renderContent(state: PlaylistInfoState.Content) {
        binding.title.text = state.playlistInfo.title
        binding.description.text = state.playlistInfo.description
        binding.playlistCount.text = TrackCountFormatter.formatTrackCount(state.playlistInfo.count)
        binding.playlistTime.text = TimeFormatter.formatDuration(state.playlistInfo.duration)
        Glide.with(requireContext())
            .load(state.playlistInfo.image)
            .centerCrop()
            .placeholder(R.drawable.place_holder_cover)
            .signature(ObjectKey(File(state.playlistInfo.image!!.path).lastModified()))
            .into(binding.imagePlaylist)

        renderContentTracks(state.tracks)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
