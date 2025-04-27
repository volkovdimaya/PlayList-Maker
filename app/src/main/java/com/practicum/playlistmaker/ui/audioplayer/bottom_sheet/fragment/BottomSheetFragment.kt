package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.BottomSheetBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.bottomSheetPlaylistDelegate
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.BottomSheetClickState
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.BottomSheetState
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.PlaylistBottomSheetItem
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model.AudioPlayerEventFromBottomSheet
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model.BottomSheetViewModel
import com.practicum.playlistmaker.ui.share_data.SharedTrackViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetFragment() : BottomSheetDialogFragment() {

    private val sharedTrackViewModel: SharedTrackViewModel by activityViewModel()
    private var track : Track? = null


    private var _binding: BottomSheetBinding? = null
    private val binding
        get() = _binding!!


    private val adapter = ListDelegationAdapter(
        bottomSheetPlaylistDelegate(object : PlaylistItemClickListenerBottomSheet {
            override fun onPlaylistItemClick(item: PlaylistBottomSheetItem) {
                viewModel.addTrackToPlaylist( track!!,
                    playListAndTrack = PlayListAndTrack(
                        trackId = track!!.trackId,
                        playlistId = item.id,
                    ), title = item.title
                )
            }
        })
    )


    private val viewModel by viewModel<BottomSheetViewModel>()

    private val sharedViewModel: AudioPlayerEventFromBottomSheet by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerPlaylistSheet.adapter = adapter
        binding.recyclerPlaylistSheet.layoutManager = LinearLayoutManager(requireContext())

        binding.btnNewPlaylist.setOnClickListener {
            dismiss()
            findNavController().navigate(
                R.id.action_audioPlayerFragment_to_addNewPlayListFragment
            )
        }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.state_click.observe(viewLifecycleOwner) {
            clickItem(it)
        }
        lifecycleScope.launch {
            sharedTrackViewModel.trackFlow.collect { shareTrack ->
                track = shareTrack
            }
        }

    }

    private fun clickItem(item: BottomSheetClickState) {
        when (item) {
            is BottomSheetClickState.TrackAddPlaylist -> {
                sharedViewModel.eventTrackAddPlaylist(item.title)
                dismiss()
            }
            is BottomSheetClickState.TrackNotAddPlaylist -> {
                Snackbar.make(
                    requireView(),
                    "Трек уже добавлен в плейлист ${item.title}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun render(it: BottomSheetState) {

        when (it) {
            BottomSheetState.idle -> showEmpty()
            is BottomSheetState.Content -> renderContent(it.playlistItem)
        }

    }

    private fun renderContent(playlist: List<PlaylistBottomSheetItem>) {
        binding.recyclerPlaylistSheet.layoutManager = LinearLayoutManager(requireContext())

        adapter.items = playlist
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

interface PlaylistItemClickListenerBottomSheet {
    fun onPlaylistItemClick(item: PlaylistBottomSheetItem)
}