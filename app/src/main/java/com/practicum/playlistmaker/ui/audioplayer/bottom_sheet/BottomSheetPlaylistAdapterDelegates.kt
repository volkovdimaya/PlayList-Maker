package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet



import com.bumptech.glide.Glide
import com.practicum.playlistmaker.RVItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistSheetBinding
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.fragment.PlaylistItemClickListenerBottomSheet
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.PlaylistBottomSheetItem


fun bottomSheetPlaylistDelegate( itemClickListener: PlaylistItemClickListenerBottomSheet) = adapterDelegateViewBinding<PlaylistBottomSheetItem, RVItem, ItemPlaylistSheetBinding>(
    { layoutInflater, root -> ItemPlaylistSheetBinding.inflate(layoutInflater, root, false) }
) {


    bind {
        binding.count.text = item.trackCount.toString()
        binding.title.text = item.title


        binding.root.setOnClickListener {
            itemClickListener.onPlaylistItemClick(item)
        }

        Glide.with(itemView)
            .load(item.image)
            .centerCrop()
            .placeholder(R.drawable.place_holder_cover)
            .into(binding.image)
    }
}