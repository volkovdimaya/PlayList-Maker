package com.practicum.playlistmaker.ui.search.activity

import androidx.core.widget.addTextChangedListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.practicum.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.practicum.playlistmaker.ui.search.models.SearchState
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.TrackAdapter


const val TRACK_DETAILS = "TRACK_DETAILS"

class SearchActivity : AppCompatActivity() {



    private lateinit var progressBar: ProgressBar
    private lateinit var noContentPlaceHolder: LinearLayout
    private lateinit var noInternetPlaceHolder: LinearLayout
    private lateinit var recyclerViewTrak: RecyclerView
    private lateinit var search: EditText


    private lateinit var linearLayoutHistory: LinearLayout
    private lateinit var recyclerViewHistoryTrack: RecyclerView

    private lateinit var binding: ActivitySearchBinding



    private val viewModel by viewModels<TrackSearchViewModel> {
        TrackSearchViewModel.provideFactory(
            InteractorSearchHistory(Creator.provideInteractorSearchHistory()),
            Creator.provideTracksInteractor()
        )
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)


        setContentView(binding.root)

        progressBar = binding.progressBar
        noContentPlaceHolder = binding.errorNoContent
        noInternetPlaceHolder = binding.errorInternet
        recyclerViewTrak = binding.recyclerTrack
        search = binding.search
        linearLayoutHistory = binding.linearLayoutHistory
        recyclerViewHistoryTrack = binding.recyclerHistoryTrack

        recyclerViewTrak.layoutManager = LinearLayoutManager(this)
        recyclerViewHistoryTrack.layoutManager = LinearLayoutManager(this)

        val trakAdapter = TrackAdapter(emptyList(), ::clickOnTrack)
        val historyAdapter = TrackAdapter(emptyList(), ::clickOnTrack)


        recyclerViewTrak.adapter = trakAdapter
        recyclerViewHistoryTrack.adapter = historyAdapter

        observeViewModel()


        //trackSearchPresenter.onCreate()
        //linearLayoutHistory.isVisible = trackSearchPresenter.showHistory() == true

        //val clearButton: ImageView = findViewById(R.id.clearIcon)

        binding.search.addTextChangedListener {
            viewModel.updateReqvest(it.toString())
        }

        binding.clearIcon.setOnClickListener {
            binding.search.text.clear()
            viewModel.clearSearch()
        }


        /* скорее что то и надо
        search.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                clearButton.isVisible = (!s.isNullOrEmpty())

                if (search.hasFocus() && s?.isEmpty() == true) {
                    progressBar.visibility = View.GONE
                    noContentPlaceHolder.visibility = View.GONE
                    noInternetPlaceHolder.visibility = View.GONE

                    trackSearchPresenter.addTextChangedListener()

                    linearLayoutHistory.isVisible = trackSearchPresenter.showHistory() == true

                } else {
                    linearLayoutHistory.visibility = View.GONE
                    trackSearchPresenter.searchDebounce(s.toString())
                }
            },
        )
        */

        binding.search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.search.text.isEmpty()) {
                viewModel.onSearchFocusGained()
            }
        }
        binding.clearIcon.setOnClickListener {
            binding.search.text.clear()
            viewModel.clearSearch()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
/*

        search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && search.text.isEmpty()) {
                linearLayoutHistory.isVisible = trackSearchPresenter.showHistory() == true
            }
        }
        */
/*
        clearButton.setOnClickListener {
            search.setText("")

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)

            trackSearchPresenter.clearSearch()

        }
        */

        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.updateReqvest(search.text.toString())
               // trackSearchPresenter.loadTrack(search.text.toString())
            }
            false
        }

        val updateBtn = findViewById<Button>(R.id.btn_search_update)
        updateBtn.setOnClickListener {
            Log.d("1111112121", "updateBtn")
            viewModel.loadTrack(search.text.toString())
           // trackSearchPresenter.loadTrack(search.text.toString()) непонятно!!!!
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_search)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val btnClearHistory = findViewById<Button>(R.id.btn_clear_history)
        btnClearHistory.setOnClickListener {
           // trackSearchPresenter.clearHistory()
            linearLayoutHistory.visibility = View.GONE
        }
    }

    private fun showProgressBar() {
        recyclerViewTrak.isVisible = false
        progressBar.isVisible = true
        noInternetPlaceHolder.isVisible = false
        noContentPlaceHolder.isVisible = false
        binding.linearLayoutHistory.isVisible = false

        Log.d("121212", "showProgressBar")
    }

    private fun showNoInternet() {
        noInternetPlaceHolder.visibility = View.VISIBLE
        noContentPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.GONE
        binding.linearLayoutHistory.isVisible = false

        Log.d("121212", "showNoInternet")
    }

    private fun showNoContent() {
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        binding.linearLayoutHistory.isVisible = false

        Log.d("1111112121", "showNoContent")
    }
    private fun showContentHistory()
    {
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.GONE
        progressBar.visibility = View.GONE
        binding.linearLayoutHistory.isVisible = true
        Log.d("1111112121", "showContentHistory")
    }

    private fun showTracks() {



        progressBar.visibility = View.GONE
        recyclerViewTrak.visibility = View.VISIBLE

        Log.d("121212", "showTracks")
    }

    fun clickOnTrack(track: Track) {
        viewModel.onTrackClicked(track)

        Log.d("AudioPlayerActivity", "clickOnTrack")
    }

    fun render(state: SearchState) {
        when (state) {
            is SearchState.NotContent -> showNoContent()
            is SearchState.Content -> showTracks()
            is SearchState.ProgressBar -> showProgressBar()
            is SearchState.NoInternet -> showNoInternet()
            is SearchState.ContentHistory -> showContentHistory()
        }
    }


    private fun observeViewModel() {

        viewModel.searchState.observe(this) {
            render(it)
        }

        viewModel.tracks.observe(this) { tracks ->
            (binding.recyclerTrack.adapter as TrackAdapter).updateData(tracks)
        }

        viewModel.historyTracks.observe(this) { historyTracks ->
            (binding.recyclerHistoryTrack.adapter as TrackAdapter).updateData(historyTracks)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        /*
        viewModel.showHistory.observe(this) { showHistory ->
            binding.linearLayoutHistory.isVisible = showHistory
        }
        */




        viewModel.showBtnClear.observe(this) { showBtnClear ->
            binding.clearIcon.isVisible = showBtnClear
        }



        viewModel.navigateToTrackDetails.observe(this) { track ->
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_DETAILS, track)
            startActivity(intent)
        }
    }
}

