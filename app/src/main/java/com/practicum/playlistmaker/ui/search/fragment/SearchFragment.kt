package com.practicum.playlistmaker.ui.search.fragment

import androidx.core.widget.addTextChangedListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.practicum.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.practicum.playlistmaker.ui.search.models.SearchState
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.TrackAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


const val TRACK_DETAILS = "TRACK_DETAILS"

class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var noContentPlaceHolder: LinearLayout
    private lateinit var noInternetPlaceHolder: LinearLayout
    private lateinit var recyclerViewTrak: RecyclerView
    private lateinit var search: EditText

    private lateinit var linearLayoutHistory: LinearLayout
    private lateinit var recyclerViewHistoryTrack: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val viewModel by viewModel<TrackSearchViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        noContentPlaceHolder = binding.errorNoContent
        noInternetPlaceHolder = binding.errorInternet
        recyclerViewTrak = binding.recyclerTrack
        search = binding.search
        linearLayoutHistory = binding.linearLayoutHistory
        recyclerViewHistoryTrack = binding.recyclerHistoryTrack

        recyclerViewTrak.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewHistoryTrack.layoutManager = LinearLayoutManager(requireContext())

        val trakAdapter = TrackAdapter(emptyList(), ::clickOnTrack)
        val historyAdapter = TrackAdapter(emptyList(), ::clickOnTrack)


        recyclerViewTrak.adapter = trakAdapter
        recyclerViewHistoryTrack.adapter = historyAdapter

        observeViewModel()

        binding.search.addTextChangedListener {
            viewModel.updateRequest(it.toString())
        }

        binding.search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.search.text.isEmpty()) {
                viewModel.onSearchFocusGained()
            }
        }
        binding.clearIcon.setOnClickListener {
            binding.search.text.clear()
            viewModel.clearSearch()
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.updateRequest(search.text.toString())
            }
            false
        }


        binding.btnSearchUpdate.setOnClickListener {
            viewModel.loadTrack(search.text.toString())
        }


        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun showProgressBar() {
        recyclerViewTrak.isVisible = false
        progressBar.isVisible = true
        noInternetPlaceHolder.isVisible = false
        noContentPlaceHolder.isVisible = false
        binding.linearLayoutHistory.isVisible = false
    }

    private fun showNoInternet() {
        noInternetPlaceHolder.visibility = View.VISIBLE
        noContentPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.GONE
        binding.linearLayoutHistory.isVisible = false
    }

    private fun showNoContent() {
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        binding.linearLayoutHistory.isVisible = false
    }

    private fun showContentHistory(history: List<Track>) {
        (binding.recyclerHistoryTrack.adapter as TrackAdapter).updateData(history)
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.GONE
        progressBar.visibility = View.GONE
        binding.linearLayoutHistory.isVisible = true
    }

    private fun showEmpty() {
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.GONE
        progressBar.visibility = View.GONE
        binding.linearLayoutHistory.isVisible = false

    }

    private fun showTracks(tracks: List<Track>) {
        (binding.recyclerTrack.adapter as TrackAdapter).updateData(tracks)
        progressBar.visibility = View.GONE
        recyclerViewTrak.visibility = View.VISIBLE
    }

    private fun clickOnTrack(track: Track) {
        viewModel.onTrackClicked(track)
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.NotContent -> showNoContent()
            is SearchState.Content -> showTracks(state.tracks)
            is SearchState.ProgressBar -> showProgressBar()
            is SearchState.NoInternet -> showNoInternet()
            is SearchState.ContentHistory -> showContentHistory(state.history)
            is SearchState.Empty -> showEmpty()
            is SearchState.BtnClear -> showBtnClear(state.visible)
        }
    }

    private fun showBtnClear(visible: Boolean) {
        binding.clearIcon.isVisible = visible
    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            viewModel.searchState.collect{
                render(it)
            }
        }

        viewModel.navigateToTrackDetails.observe(viewLifecycleOwner) { track ->
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_DETAILS, track)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}

