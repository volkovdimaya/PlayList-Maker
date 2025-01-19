package com.practicum.playlistmaker.ui.search


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.mapper.TrackMapper

class TrackAdapter(
    private var tracks: List<Track>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolderTrack>() {
    fun interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrack {
        return ViewHolderTrack(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: ViewHolderTrack, position: Int) {

        holder.bind(TrackMapper.mapToTrackShort(tracks[position]))
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(tracks[position])
        }
    }

    fun updateData(newSongs: List<Track>) {
        tracks = newSongs
        notifyDataSetChanged()
    }
}





