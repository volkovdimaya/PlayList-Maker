package com.practicum.playlistmaker.ui.audioplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.TrackAudioPlayer
import com.practicum.playlistmaker.ui.search.TRACK_DETAILS


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var audioPlayerManager: MediaPlayerInteractor

    private lateinit var btnActive: ImageView
    private lateinit var playbackTime: TextView
    private var track: TrackAudioPlayer? = null

    private var secondsCount: Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private var countingDownRunnable = Runnable {
        updatePlaybackTime()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        audioPlayerManager = MediaPlayerInteractor(MediaPlayer())

        btnActive = findViewById(R.id.play)

        playbackTime = findViewById(R.id.playback_time)

        val toolbar: Toolbar = findViewById(R.id.toolbar_audio_player)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        track = intent.getSerializableExtra(TRACK_DETAILS) as? TrackAudioPlayer

        track?.let {
            displayTrackData(it)
            audioPlayerManager.preparePlayer(it.previewUrl, onPrepared = {
                btnActive.setImageResource(R.drawable.btn_play)
            }, onCompletion = {
                handler.removeCallbacks(countingDownRunnable)
                btnActive.setImageResource(R.drawable.btn_play)
                secondsCount = 0
                playbackTime.text = String.format("%d:%02d", secondsCount / 60, secondsCount % 60)
            })
        }

        if (track == null) {
            Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show()
            finish()
        }

        btnActive.setOnClickListener {
            audioPlayerManager.togglePlayback(
                onPlay = {
                    btnActive.setImageResource(R.drawable.btn_pause)
                    handler.postDelayed(countingDownRunnable, SECOND)
                },
                onPause = {
                    btnActive.setImageResource(R.drawable.btn_play)
                    handler.removeCallbacks(countingDownRunnable)
                }
            )
        }
    }

    private fun pausePlayback() {
        audioPlayerManager.pausePlayer {
            handler.removeCallbacks(countingDownRunnable)
            btnActive.setImageResource(R.drawable.btn_play)

        }
    }

    private fun updatePlaybackTime() {
        secondsCount = audioPlayerManager.getCurrentPosition() / 1000L
        playbackTime.text = String.format("%d:%02d", secondsCount / 60, secondsCount % 60)
        handler.postDelayed(countingDownRunnable, SECOND)
    }

    override fun onPause() {
        super.onPause()
        pausePlayback()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerManager.release()
    }

    private fun displayTrackData(track: TrackAudioPlayer) {
        findViewById<TextView>(R.id.track_name).text = track.trackName
        findViewById<TextView>(R.id.artist_name).text = track.artistName
        findViewById<TextView>(R.id.album).text = track.collectionName
        findViewById<TextView>(R.id.year).text = track.releaseDate.take(4)
        findViewById<TextView>(R.id.genre).text = track.primaryGenreName
        findViewById<TextView>(R.id.country).text = track.country

        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/', getString(R.string.image_resolution)))
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.place_holder_cover)
            .into(findViewById(R.id.cover))
    }

    companion object {
        private const val SECOND = 1000L
    }
}