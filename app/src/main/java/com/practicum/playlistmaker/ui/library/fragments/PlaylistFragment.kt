package com.practicum.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.library.models.PlaylistState
import com.practicum.playlistmaker.ui.library.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(){
    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private val viewModel by viewModel<PlaylistViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun render(it: PlaylistState) {
        when(it){
            PlaylistState.PlaylistEmpty -> showEmpty()
        }
    }

    private fun showEmpty() {
       binding.emptyPlaylist.isVisible = true
    }
}

