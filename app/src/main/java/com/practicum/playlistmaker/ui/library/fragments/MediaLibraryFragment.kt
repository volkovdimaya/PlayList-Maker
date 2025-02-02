package com.practicum.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMedialibraryBinding
import com.practicum.playlistmaker.ui.library.LibraryViewPagerAdapter

class MediaLibraryFragment : Fragment() {

    private var _binding: FragmentMedialibraryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = LibraryViewPagerAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourites_tracks)
                1 -> tab.text = getString(R.string.playlists    )
            }
        }

        tabMediator.attach()



    }

    override fun onDestroy() {
        super.onDestroy()
        //tabMediator.detach()
        _binding = null
    }
}








