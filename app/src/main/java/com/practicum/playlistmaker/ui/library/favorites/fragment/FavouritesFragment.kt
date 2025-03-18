package com.practicum.playlistmaker.ui.library.favorites.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.practicum.playlistmaker.ui.library.favorites.view_model.FavouritesViewModel
import com.practicum.playlistmaker.ui.library.models.FavouritesState
import com.practicum.playlistmaker.ui.search.TrackAdapter
import com.practicum.playlistmaker.ui.search.fragment.TRACK_DETAILS
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private val viewModel by viewModel<FavouritesViewModel>()

    private lateinit var recyclerViewTrak: RecyclerView

    companion object {
        fun newInstance() = FavouritesFragment()
    }

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        val trakAdapter = TrackAdapter(emptyList(), ::clickOnTrack)

        recyclerViewTrak = binding.favoritesTrack

        recyclerViewTrak.layoutManager = LinearLayoutManager(requireContext())

        recyclerViewTrak.adapter = trakAdapter

        viewModel.navigateToTrackDetails.observe(viewLifecycleOwner) { track ->
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_DETAILS, track)
            startActivity(intent)
        }
    }

    private fun render(it: FavouritesState) {
        when(it){
            is FavouritesState.FavouritesEmpty -> showEmpty()
            is FavouritesState.Content -> showListFavorites(it.tracks)
        }
    }

    private fun showListFavorites(tracks: List<Track>) {
        (recyclerViewTrak.adapter as TrackAdapter).updateData(tracks)
        binding.emptyFavourites.isVisible = false
        recyclerViewTrak.isVisible = true
    }

    private fun showEmpty() {
        binding.emptyFavourites.isVisible = true
        recyclerViewTrak.isVisible = false
    }

    private fun clickOnTrack(track: Track) {
        viewModel.onTrackClicked(track)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        viewModel.fillData()
        super.onResume()
    }
}