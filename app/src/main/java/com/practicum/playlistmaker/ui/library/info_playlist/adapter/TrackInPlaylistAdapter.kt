package com.practicum.playlistmaker.ui.library.info_playlist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.mapper.TrackMapper
import com.practicum.playlistmaker.ui.search.ViewHolderTrack

class TrackInPlaylistAdapter(
    private var tracks: List<Track>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolderTrack>() {
    interface OnItemClickListener {
        fun onItemClick(track: Track)
        fun onItemLongClick(track: Track) : Boolean
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
        holder.itemView.setOnLongClickListener {
            itemClickListener.onItemLongClick(tracks[position])
        }
    }

    fun updateData(newSongs: List<Track>) {
        tracks = newSongs
        notifyDataSetChanged()
    }
}