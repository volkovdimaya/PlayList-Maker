package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val toolbar: Toolbar = findViewById(R.id.toolbar_audio_player)

        toolbar.setNavigationOnClickListener {
            finish()
        }


        val coverImageView = findViewById<ImageView>(R.id.cover)
        val trackNameTextView = findViewById<TextView>(R.id.track_name)
        val artistNameTextView = findViewById<TextView>(R.id.artist_name)
        val durationTextView = findViewById<TextView>(R.id.duration)
        val albumTextView = findViewById<TextView>(R.id.album)
        val yearTextView = findViewById<TextView>(R.id.year)
        val genreTextView = findViewById<TextView>(R.id.genre)
        val countryTextView = findViewById<TextView>(R.id.country)
        val groupAlbum = findViewById<Group>(R.id.group_album)

        val track = intent.getSerializableExtra(TRACK_DETAILS) as? Track
        if (track != null) {
            trackNameTextView.text = track.trackName
            artistNameTextView.text = track.artistName
            durationTextView.text = getTime(track.trackTimeMillis)
            albumTextView.text = track.collectionName
            if (track.collectionName.equals(""))
                groupAlbum.visibility = View.GONE
            yearTextView.text = track.releaseDate.substring(0, 4) // берем только год
            genreTextView.text = track.primaryGenreName
            countryTextView.text = track.country

            Glide.with(this)
                .load(
                    track.artworkUrl100.replaceAfterLast(
                        '/',
                        getString(R.string.image_resolution)
                    )
                )
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.place_holder_cover)
                .into(coverImageView)
        } else {
            Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun getTime(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}