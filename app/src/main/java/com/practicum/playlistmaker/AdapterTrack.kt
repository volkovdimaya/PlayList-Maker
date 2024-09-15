package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterTrack(private val tracks: List<Track>) : RecyclerView.Adapter<ViewHolderTrack>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrack {
        return ViewHolderTrack(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: ViewHolderTrack, position: Int) {
        holder.bind(tracks[position])
    }
}





