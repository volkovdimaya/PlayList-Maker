package com.practicum.playlistmaker.ui.audioplayer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.models.PlayStatus
import com.practicum.playlistmaker.ui.audioplayer.models.AudioPlayerScreenState
import com.practicum.playlistmaker.ui.audioplayer.view_model.TrackViewModel
import com.practicum.playlistmaker.ui.search.fragment.TRACK_DETAILS
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {

    private var _binding : ActivityAudioPlayerBinding? = null
    private val binding
        get() = _binding!!


    private val viewModel: TrackViewModel by viewModel {
        val track = intent.getSerializableExtra(TRACK_DETAILS) as? Track
            ?: throw IllegalArgumentException("Ошибка, отсутствует песня")

        parametersOf(track)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbarAudioPlayer.setNavigationOnClickListener {
            finish()
        }

        binding.btnLike.setOnClickListener {
            viewModel.clickFavorite()
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

                is AudioPlayerScreenState.IsFavorite -> {
                    renderBtnFavorite(screenState.active)
                }
            }
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
        }

        binding.play.setOnClickListener {
            viewModel.play()
        }
    }

    private fun renderBtnFavorite(active: Boolean) {
        binding.btnLike.isSelected = active
    }

    private fun changeButtonStyle(playStatus: PlayStatus?) {
        if (playStatus?.isPlaying == true) {
            binding.play.setImageResource(R.drawable.btn_pause)
        } else {
            binding.play.setImageResource(R.drawable.btn_play)
        }
        playStatus?.progress?.let {
            updatePlaybackTime(it.toInt())
        }
    }

    private fun pausePlayback() {
        viewModel.pause()
    }

    private fun updatePlaybackTime(secondsCount: Int) {
        binding.playbackTime.text = String.format("%d:%02d", secondsCount / 60, secondsCount % 60)
    }

    override fun onPause() {
        super.onPause()
        pausePlayback()
    }

    private fun displayTrackData(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.album.text = track.collectionName
        binding.year.text = track.releaseDate.take(4)
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country



        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/', getString(R.string.image_resolution)))
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.place_holder_cover)
            .into(binding.cover)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}