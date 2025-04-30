package com.practicum.playlistmaker.ui.audioplayer.bottom_sheet



import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.practicum.playlistmaker.RVItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistSheetBinding
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.fragment.PlaylistItemClickListenerBottomSheet
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.models.PlaylistBottomSheetItem
import com.practicum.playlistmaker.util.TrackCountFormatter
import java.io.File


fun bottomSheetPlaylistDelegate( itemClickListener: PlaylistItemClickListenerBottomSheet) = adapterDelegateViewBinding<PlaylistBottomSheetItem, RVItem, ItemPlaylistSheetBinding>(
    { layoutInflater, root -> ItemPlaylistSheetBinding.inflate(layoutInflater, root, false) }
) {


    bind {
        binding.count.text = TrackCountFormatter.formatTrackCount(item.trackCount)
        binding.title.text = item.title


        binding.root.setOnClickListener {
            itemClickListener.onPlaylistItemClick(item)
        }

        Glide.with(itemView)
            .load(item.image)
            .centerCrop()
            .placeholder(R.drawable.place_holder_cover)
            .signature(ObjectKey(File(item.image!!.path).lastModified()))
            .into(binding.image)
    }
}