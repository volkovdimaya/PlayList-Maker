package com.practicum.playlistmaker.ui.library.playlist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistState
import com.practicum.playlistmaker.ui.library.playlist.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(){
    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private val viewModel by viewModel<PlaylistViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val adapter = ListDelegationAdapter(
        playlistItemAdapterDelegates()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_addNewPlayListFragment)
        }
        recyclerView = binding.recyclerPlaylist

        recyclerView.adapter = adapter

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

    }

    private fun render(it: PlaylistState) {
        when(it){
            PlaylistState.PlaylistEmpty -> showEmpty()
            is PlaylistState.PlaylistContent -> renderContent(it.playlist)
        }
    }

    private fun renderContent(playlist : List<PlaylistItem>) {
        binding.emptyPlaylist.isVisible = false
        binding.recyclerPlaylist.isVisible = true
        adapter.items = playlist
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
       binding.emptyPlaylist.isVisible = true
        binding.recyclerPlaylist.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

