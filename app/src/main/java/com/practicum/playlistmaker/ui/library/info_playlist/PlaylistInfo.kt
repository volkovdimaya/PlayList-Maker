package com.practicum.playlistmaker.ui.library.info_playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistInfoBinding

class PlaylistInfo : Fragment() {
    var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!

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
//        binding.toolbarPlaylistInfo.setNavigationOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}