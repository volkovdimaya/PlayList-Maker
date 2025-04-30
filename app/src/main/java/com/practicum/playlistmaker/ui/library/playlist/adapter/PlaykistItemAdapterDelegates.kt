package com.practicum.playlistmaker.ui.library.playlist.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.RVItem
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.ui.library.playlist.PlaylistItemClickListener
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem
import com.practicum.playlistmaker.util.TrackCountFormatter
import java.io.File

fun playlistItemAdapterDelegates(itemClickListener: PlaylistItemClickListener) = adapterDelegateViewBinding<PlaylistItem, RVItem, ItemPlaylistBinding>(
{ layoutInflater, root -> ItemPlaylistBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        Glide.with(itemView)
            .load(item.image)
            .centerCrop()
            .placeholder(R.drawable.place_holder_cover)
            .signature(ObjectKey(File(item.image!!.path).lastModified()))
            .into(binding.image)

        binding.title.text = item.title


        binding.root.setOnClickListener {
            itemClickListener.onPlaylistItemClick(item)
        }

        binding.count.text = TrackCountFormatter.formatTrackCount(item.trackCount)
    }
}