package com.practicum.playlistmaker.ui.library.info_playlist


import android.os.Bundle
import android.util.Log
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.library.info_playlist.adapter.TrackInPlaylistAdapter
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfoState
import com.practicum.playlistmaker.ui.library.info_playlist.view_model.PlaylistInfoViewModel
//import com.practicum.playlistmaker.ui.share_data.SharedPlaylistViewModel
import com.practicum.playlistmaker.ui.share_data.SharedTrackViewModel
import com.practicum.playlistmaker.util.TimeFormatter
import com.practicum.playlistmaker.util.TrackCountFormatter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistInfoFragment : Fragment() {
    var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!

//    val viewModel by viewModel<PlaylistInfoViewModel>()


    private val viewModel: PlaylistInfoViewModel by activityViewModel()
    private val sharedTrackViewModel: SharedTrackViewModel by activityViewModel()
//    private val sharedPlaylist : SharedPlaylistViewModel by activityViewModel()

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
        binding.toolbarPlaylistInfo.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lifecycleScope.launch {
//                viewModel.setContent(playlist)
            viewModel.playlistItemFlow.collect { playlistItem ->
                Log.d("PlaylistInfoViewModel1", "playlistItem: $playlistItem")
                when (playlistItem) {
                    is PlaylistInfoState.Content -> renderContent(playlistItem)
                    PlaylistInfoState.EmptyPlaylist -> {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.empty_playlist),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    is PlaylistInfoState.ContentTracks -> renderContentTracks(playlistItem.tracks)
                }
            }

        }
//        val playlist = arguments?.let {
//            PlaylistInfoFragmentArgs.fromBundle(it).item
//        }
//
//        if (playlist != null) {
//            viewModel.setContent(playlist!!)
//        }

//        viewModel.state.observe(viewLifecycleOwner) { state ->
//            when (state) {
//                is PlaylistInfoState.Content -> renderContent(state)
//                PlaylistInfoState.EmptyPlaylist -> {
//                    Snackbar.make(
//                        requireView(),
//                        getString(R.string.empty_playlist),
//                        Snackbar.LENGTH_SHORT
//                    ).show()
//                }
//
//                is PlaylistInfoState.ContentTracks -> renderContentTracks(state.tracks)
//            }
//        }


        binding.more.setOnClickListener {
//            BottomSheetMoreFragment
//            viewModel.onMoreClick()
        }
        binding.share.setOnClickListener {
            viewModel.onShareClick()
        }
        binding.recyclerTracks.layoutManager = LinearLayoutManager(requireContext())
        val trakAdapter = TrackInPlaylistAdapter(
            emptyList(),
            object : TrackInPlaylistAdapter.OnItemClickListener {

                override fun onItemClick(track: Track) {
                    viewModel.onTrackClicked(track)
                }

                override fun onItemLongClick(track: Track): Boolean {
                    MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
                        .setView(createMessageView())
                        .setPositiveButton(R.string.yes) { _, _ ->
                            viewModel.onDeleteTrackClick(track)
                        }
                        .setNegativeButton(R.string.no) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                    return false
                }
            })
        binding.recyclerTracks.adapter = trakAdapter


        viewModel.navigateToTrackDetails.observe(viewLifecycleOwner) { track ->
            lifecycleScope.launch {
                sharedTrackViewModel.selectTrack(track)
            }
            findNavController().navigate(R.id.action_playlistInfo_to_audioPlayerFragment)
        }
    }

    private fun renderContentTracks(tracks: List<Track>) {
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
            .into(binding.imagePlaylist)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createMessageView(): View {
        return TextView(requireContext()).apply {
            text = "Хотите удалить трек?"
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.message_dialog
                )
            )
            textSize = 14f
            setPadding(24, 23, 8, 0)
            gravity = Gravity.START
        }
    }


}