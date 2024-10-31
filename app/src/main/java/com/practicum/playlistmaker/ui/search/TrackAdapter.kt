package com.practicum.playlistmaker.ui.search


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.mapper.TrackMapper
import com.practicum.playlistmaker.presentation.models.InfoTrackShort

class TrackAdapter(
    private var tracks: List<Track>,//вопрос не лучше хранить тут в InfoTrackShort? если да то обработка нажатий как лучше сделать
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

        holder.bind(TrackMapper.map(tracks[position]))
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(tracks[position])
        }
    }

    fun updateData(newSongs: List<Track>) {
        tracks = newSongs
        notifyDataSetChanged()
    }
}





