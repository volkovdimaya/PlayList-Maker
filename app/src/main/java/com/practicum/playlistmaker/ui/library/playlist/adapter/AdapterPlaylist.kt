package com.practicum.playlistmaker.ui.library.playlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.library.playlist.models.PlaylistItem

class AdapterPlaylist(private var playlist: List<PlaylistItem>) : RecyclerView.Adapter<AdapterPlaylist.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = playlist[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    fun updateData(newPlaylist: List<PlaylistItem>) {
        playlist = newPlaylist
        notifyDataSetChanged()
    }

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val count: TextView = itemView.findViewById(R.id.count)

        fun bind(item: PlaylistItem) {
            Glide.with(itemView)
                .load(item.image)
                .centerCrop()
                .placeholder(R.drawable.place_holder_cover)
                .into(image)
            title.text = item.title
            count.text = "${item.trackCount} треков"
        }
    }
}