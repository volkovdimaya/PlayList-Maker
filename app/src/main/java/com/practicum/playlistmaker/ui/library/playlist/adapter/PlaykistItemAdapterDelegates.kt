package com.practicum.playlistmaker.ui.library.playlist.adapter

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.RVItem
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.ui.library.playlist.PlaylistItemClickListener
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem

fun playlistItemAdapterDelegates(itemClickListener: PlaylistItemClickListener) = adapterDelegateViewBinding<PlaylistItem, RVItem, ItemPlaylistBinding>(
{ layoutInflater, root -> ItemPlaylistBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        Glide.with(itemView)
            .load(item.image)
            .centerCrop()
            .placeholder(R.drawable.place_holder_cover)
            .into(binding.image)
        binding.title.text = item.title

        val wordTrack = when (item.trackCount) {
            1 -> "трек"
            in 2..4 -> "трека"
            else -> "треков"
        }

        binding.root.setOnClickListener {
            itemClickListener.onPlaylistItemClick(item)
        }

        binding.count.text = "${item.trackCount} $wordTrack"
    }
}