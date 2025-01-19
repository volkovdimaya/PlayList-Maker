package com.practicum.playlistmaker.ui.search


import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackSearchBinding
import com.practicum.playlistmaker.ui.search.models.InfoTrackShort



class ViewHolderTrack(binding: ItemTrackSearchBinding) : RecyclerView.ViewHolder(binding.root) {

    private val trackName: TextView = binding.trackName
    private val artistName: TextView = binding.artistName
    private val trackTime: TextView = binding.trackTime
    private val artworkUrl100: ImageView = binding.iconTrack

    constructor(parent: ViewGroup) : this(
        ItemTrackSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(model: InfoTrackShort) {

        if (model == null)
            return
        val widthInPx = dpToPx(45f, itemView.context)
        val heightInPx = dpToPx(45f, itemView.context)

        trackName.text = model.trackName
        artistName.text = model.artistName



        trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .override(widthInPx, heightInPx)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.play_ic)
            .into(artworkUrl100)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}

