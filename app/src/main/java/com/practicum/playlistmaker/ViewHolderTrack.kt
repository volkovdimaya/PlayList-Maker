package com.practicum.playlistmaker

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.lang.Exception

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
        val widthInPx = dpToPx(45f, itemView.context)
        val heightInPx = dpToPx(45f, itemView.context)

        trackName.setText(model.trackName)
        artistName.setText(model.artistName)



        trackTime.setText(model.trackTime)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .override(widthInPx, heightInPx)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.play_ic)
            .into(artworkUrl100)

        //не понял как лучше сделать
        //в случии изменении текста установливаю вес для артиста
        //Обратите внимание, что не только название трека может не помещаться на экране, но и имя артиста/группы.
        artistName.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                artistName.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val visibleText = artistName.layout.text
                if (!visibleText.equals(model.artistName)) {
                    val layoutParams = artistName.layoutParams as LinearLayout.LayoutParams
                    layoutParams.width = 0
                    layoutParams.weight = 1f
                    artistName.layoutParams = layoutParams
                }
            }
        })
    }

    //открыть фигму посмотреть размеры
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}

