package com.practicum.playlistmaker.ui.audioplayer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.fragment.BottomSheetFragment
import com.practicum.playlistmaker.ui.audioplayer.bottom_sheet.view_model.AudioPlayerEventFromBottomSheet
import com.practicum.playlistmaker.ui.audioplayer.models.PlayStatus
import com.practicum.playlistmaker.ui.audioplayer.models.AudioPlayerScreenState
import com.practicum.playlistmaker.ui.audioplayer.view_model.TrackViewModel
import com.practicum.playlistmaker.ui.share_data.SharedTrackViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding
        get() = _binding!!

    private val sharedTrackViewModel: SharedTrackViewModel by activityViewModel()

    private val viewModel: TrackViewModel by viewModel()

    private val sharedViewModel: AudioPlayerEventFromBottomSheet by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbarAudioPlayer.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnLike.setOnClickListener {
            viewModel.clickFavorite()
        }



        sharedViewModel.event.observe(viewLifecycleOwner) { title ->
            Snackbar.make(
                binding.root,
                "Добавлено в плейлист ${title}",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        viewModel.screenStateLiveData.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is AudioPlayerScreenState.Content -> {
                    displayTrackData(screenState.trackModel)
                    renderBtnFavorite(screenState.isFavorite)
                }

                is AudioPlayerScreenState.Error -> {

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }

                AudioPlayerScreenState.idle -> {

                }
            }
        }

        viewModel.getPlayStatusLiveData().observe(viewLifecycleOwner) { playStatus ->
            changeButtonStyle(playStatus)
        }

        binding.play.setOnClickListener {
            viewModel.play()
        }
        binding.btnAdd.setOnClickListener {
            BottomSheetFragment().show(parentFragmentManager, "MyBottomSheet")

        }
        lifecycleScope.launch {
            sharedTrackViewModel.trackFlow.collect { track ->
                viewModel.loadContent(track)
            }
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