package com.practicum.playlistmaker.ui.search

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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.presentation.models.TrackAudioPlayer
import com.practicum.playlistmaker.presentation.search.TrackSearchPresenter
import com.practicum.playlistmaker.presentation.search.SearchView
import com.practicum.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.practicum.playlistmaker.ui.search.models.SearchState


const val TRACK_DETAILS = "TRACK_DETAILS"

class SearchActivity : AppCompatActivity(), SearchView {
    private lateinit var trackSearchPresenter: TrackSearchPresenter

    private lateinit var progressBar: ProgressBar
    private lateinit var noContentPlaceHolder: LinearLayout
    private lateinit var noInternetPlaceHolder: LinearLayout
    private lateinit var recyclerViewTrak: RecyclerView
    private lateinit var search: EditText


    private lateinit var linearLayoutHistory: LinearLayout
    private lateinit var recyclerViewHistoryTrack: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.progressBar)
        noContentPlaceHolder = findViewById(R.id.error_no_content)
        noInternetPlaceHolder = findViewById(R.id.error_internet)
        recyclerViewTrak = findViewById(R.id.recycler_track)
        search = findViewById(R.id.search)
        linearLayoutHistory = findViewById(R.id.linear_layout_history)
        recyclerViewHistoryTrack = findViewById(R.id.recycler_history_track)

        recyclerViewTrak.layoutManager = LinearLayoutManager(this)
        recyclerViewHistoryTrack.layoutManager = LinearLayoutManager(this)

        trackSearchPresenter = Creator.provideTrackSearchController(this)

        recyclerViewTrak.adapter = trackSearchPresenter.getTrackAdapter()
        recyclerViewHistoryTrack.adapter = trackSearchPresenter.getTrackHistoryAdapter()

        trackSearchPresenter.onCreate()
        linearLayoutHistory.isVisible = trackSearchPresenter.showHistory()

        val clearButton: ImageView = findViewById(R.id.clearIcon)


        search.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                clearButton.isVisible = (!s.isNullOrEmpty())

                if (search.hasFocus() && s?.isEmpty() == true) {
                    progressBar.visibility = View.GONE
                    noContentPlaceHolder.visibility = View.GONE
                    noInternetPlaceHolder.visibility = View.GONE

                    trackSearchPresenter.addTextChangedListener()

                    linearLayoutHistory.isVisible = trackSearchPresenter.showHistory()

                } else {
                    linearLayoutHistory.visibility = View.GONE
                    trackSearchPresenter.searchDebounce(s.toString())
                }
            },
        )

        search.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && search.text.isEmpty()) {
                linearLayoutHistory.isVisible = trackSearchPresenter.showHistory()
            }
        }

        clearButton.setOnClickListener {
            search.setText("")

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)

            trackSearchPresenter.clearSearch()

        }

        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearchPresenter.loadTrack(search.text.toString())
            }
            false
        }
        val updateBtn = findViewById<Button>(R.id.btn_search_update)
        updateBtn.setOnClickListener {
            trackSearchPresenter.loadTrack(search.text.toString())
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_search)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val btnClearHistory = findViewById<Button>(R.id.btn_clear_history)
        btnClearHistory.setOnClickListener {
            trackSearchPresenter.clearHistory()
            linearLayoutHistory.visibility = View.GONE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        trackSearchPresenter.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        trackSearchPresenter.onSaveInstanceState(outState)
    }

    fun showProgressBar() {
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        noInternetPlaceHolder.visibility = View.GONE
        noContentPlaceHolder.visibility = View.GONE
    }

    fun showNoInternet() {
        noInternetPlaceHolder.visibility = View.VISIBLE
        noContentPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    fun showNoContent() {
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun showTracks() {
        progressBar.visibility = View.GONE
        recyclerViewTrak.visibility = View.VISIBLE
    }

    override fun clickOnTrack(track: TrackAudioPlayer) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(TRACK_DETAILS, track)
        startActivity(intent)
    }

    override fun render(state: SearchState) {
        when(state) {
           is SearchState.NotContent -> showNoContent()
            is SearchState.Content -> showTracks()
            is SearchState.ProgressBar -> showProgressBar()
            is SearchState.NoInternet -> showNoInternet()
        }
    }


}

