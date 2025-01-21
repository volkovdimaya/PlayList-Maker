package com.practicum.playlistmaker.ui.library.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMedialibraryBinding
import com.practicum.playlistmaker.ui.library.LibraryViewPagerAdapter

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialibraryBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourites_tracks)
                1 -> tab.text = getString(R.string.playlists    )
            }
        }
        tabMediator.attach()

        binding.toolbarLibrary.setNavigationOnClickListener {
            onBackPressed()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}








