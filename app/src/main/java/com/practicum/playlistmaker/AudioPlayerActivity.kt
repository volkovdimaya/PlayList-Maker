package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
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

    private var playerState = STATE_DEFAULT

    private lateinit var btnActive: ImageView
    private lateinit var playbackTime: TextView
    private var mediaPlayer = MediaPlayer()

    private var secondsCount: Long = 0
    private var durationTrack : Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private var countingDownRunnable = Runnable {
        countingDownDebounce()

        secondsCount = (mediaPlayer.currentPosition/1000).toLong()
        playbackTime.text = String.format("%d:%02d", secondsCount / 60, secondsCount % 60)

    }
    private var track: Track? = null

    private fun countingDownDebounce() {
        handler.postDelayed(countingDownRunnable, SECOND)
    }

    private fun removeCountingDownDebounce() {
        handler.removeCallbacks(countingDownRunnable)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        btnActive = findViewById(R.id.play)

        playbackTime = findViewById(R.id.playback_time)

        btnActive.setOnClickListener {
            playbackControl()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_audio_player)

        toolbar.setNavigationOnClickListener {
            finish()
        }


        val coverImageView = findViewById<ImageView>(R.id.cover)
        val trackNameTextView = findViewById<TextView>(R.id.track_name)
        val artistNameTextView = findViewById<TextView>(R.id.artist_name)
        val albumTextView = findViewById<TextView>(R.id.album)
        val yearTextView = findViewById<TextView>(R.id.year)
        val genreTextView = findViewById<TextView>(R.id.genre)
        val countryTextView = findViewById<TextView>(R.id.country)
        val groupAlbum = findViewById<Group>(R.id.group_album)

        track = intent.getSerializableExtra(TRACK_DETAILS) as? Track

        track?.let {

            preparePlayer()
            trackNameTextView.text = it.trackName
            artistNameTextView.text = it.artistName
            albumTextView.text = it.collectionName
            if (it.collectionName.isEmpty())
                groupAlbum.visibility = View.GONE
            yearTextView.text = it.releaseDate.substring(0, 4) // берем только год
            genreTextView.text = it.primaryGenreName
            countryTextView.text = it.country

            Glide.with(this)
                .load(
                    it.artworkUrl100.replaceAfterLast(
                        '/',
                        getString(R.string.image_resolution)
                    )
                )
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.place_holder_cover)
                .into(coverImageView)

        }
        if (track == null) {
            Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show()
            finish()
        }

        mediaPlayer.setOnCompletionListener {
            pausePlayer()
            removeCountingDownDebounce()
            secondsCount = 0
            playbackTime.text = String.format("%d:%02d", secondsCount / 60, secondsCount % 60)
        }
    }

    fun getTime(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val SECOND = 1000L
    }

    private fun preparePlayer() {
        if (track != null) {
            mediaPlayer.setDataSource(track?.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED

            }
        }

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {

        durationTrack = (mediaPlayer.duration/1000).toLong()
        mediaPlayer.start()
        countingDownDebounce()
        btnActive.setImageResource(R.drawable.btn_pause)

        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        handler.removeCallbacks(countingDownRunnable)
        mediaPlayer.pause()
        btnActive.setImageResource(R.drawable.btn_play)
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler.removeCallbacks(countingDownRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(countingDownRunnable)
    }
}