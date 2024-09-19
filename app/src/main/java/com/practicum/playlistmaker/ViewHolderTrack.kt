package com.practicum.playlistmaker


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class ViewHolderTrack(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView
    private val artworkUrl100: ImageView

    init {
        trackName = itemView.findViewById(R.id.track_name)
        artistName = itemView.findViewById(R.id.artist_name)
        trackTime = itemView.findViewById(R.id.track_time)
        artworkUrl100 = itemView.findViewById(R.id.icon_track)


    }

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(R.layout.item_track_search, parent, false)
    )


    fun bind(model: Track) {

        if (model == null)
            return
        val widthInPx = dpToPx(45f, itemView.context)
        val heightInPx = dpToPx(45f, itemView.context)

        trackName.setText(model.trackName)
        artistName.setText(model.artistName)



        trackTime.setText(getTime(model.trackTimeMillis))
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .override(widthInPx, heightInPx)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.play_ic)
            .into(artworkUrl100)

        itemView.setOnClickListener {
            val sharedPrefs = itemView.context.getSharedPreferences(PLAYLIST_MAKER, MODE_PRIVATE)

           val searchHistory = SearchHistory(sharedPrefs)
            searchHistory.read()
            searchHistory.update(model)
            searchHistory.write()


            Log.d("ViewHolderTrack", "Response: ${searchHistory.songs.toString()}")


        }

    }

    fun getTime(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }


    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }


    fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_LIST_TRACK, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }


    fun write(sharedPreferences: SharedPreferences, track: Array<Track>) {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(HISTORY_LIST_TRACK, json)
            .apply()
    }

    fun moveTrackToLast(tracks: Array<Track>, model: Track): Array<Track> {

        val trackIndex = tracks.indexOfFirst { it.trackId.equals( model.trackId) }

        if (trackIndex != -1) {
            val mutableTracks = tracks.toMutableList()
            val track = mutableTracks.removeAt(trackIndex)
            mutableTracks.add(track)
            return mutableTracks.toTypedArray()
        }
        val size = tracks.size
        val mutableTracks = tracks.toMutableList()
        if(size < 10)
            mutableTracks.add(model)
        else
            mutableTracks[9] = model
        return mutableTracks.toTypedArray()
    }
}

