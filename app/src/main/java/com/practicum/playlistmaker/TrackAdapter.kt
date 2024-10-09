package com.practicum.playlistmaker


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private var tracks: List<Track>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolderTrack>() {

    interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrack {
        return ViewHolderTrack(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: ViewHolderTrack, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(tracks[position])
        }
    }

    fun updateData(newSongs: List<Track>) {

        tracks = newSongs
        notifyDataSetChanged()
    }

    fun updateDataHistory(newSongs: List<Track>) {
        tracks = newSongs.reversed()
        notifyDataSetChanged()

    }
}





