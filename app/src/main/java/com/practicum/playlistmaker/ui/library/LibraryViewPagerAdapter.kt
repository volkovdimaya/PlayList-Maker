package com.practicum.playlistmaker.ui.library

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.ui.library.favorites.fragment.FavouritesFragment
import com.practicum.playlistmaker.ui.library.fragments.PlaylistFragment


class LibraryViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavouritesFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }


}


