package com.practicum.playlistmaker.ui.audioplayer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.ui.audioplayer.models.PlayStatus
import com.practicum.playlistmaker.domain.player.models.TrackAudioPlayer
import com.practicum.playlistmaker.ui.audioplayer.models.AudioPlayerScreenState
import com.practicum.playlistmaker.ui.audioplayer.view_model.TrackViewModel
import com.practicum.playlistmaker.ui.search.activity.TRACK_DETAILS


class AudioPlayerActivity : AppCompatActivity() {


    private val viewModel: TrackViewModel by viewModels {
        val track = intent.getSerializableExtra(TRACK_DETAILS) as? TrackAudioPlayer
            ?: throw IllegalArgumentException("Ошибка, отсутствует песня")
        TrackViewModel.getViewModelFactory(
            track,
            Creator.providerTrackPlayer(track.previewUrl)
        )

    }

    private lateinit var btnActive: ImageView
    private lateinit var playbackTime: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        btnActive = findViewById(R.id.play)

        playbackTime = findViewById(R.id.playback_time)

        val toolbar: Toolbar = findViewById(R.id.toolbar_audio_player)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.screenStateLiveData.observe(this) { screenState ->
            when (screenState) {
                is AudioPlayerScreenState.Content -> {
                    displayTrackData(screenState.trackModel)
                }

                is AudioPlayerScreenState.Error -> {
                    Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
        }

        btnActive.setOnClickListener {
            viewModel.play()
        }
    }

    private fun changeButtonStyle(playStatus: PlayStatus?) {
        if (playStatus?.isPlaying == true) {
            btnActive.setImageResource(R.drawable.btn_pause)
        } else {
            btnActive.setImageResource(R.drawable.btn_play)
        }
        playStatus?.progress?.let {
            updatePlaybackTime(it.toInt())
        }
    }

    private fun pausePlayback() {
        viewModel.pause()
    }

    private fun updatePlaybackTime(secondsCount: Int) {
        playbackTime.text = String.format("%d:%02d", secondsCount / 60, secondsCount % 60)
    }

    override fun onPause() {
        super.onPause()
        pausePlayback()
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
}