package com.practicum.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.models.Track


const val TRACK_DETAILS = "TRACK_DETAILS"

class SearchActivity : AppCompatActivity() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable = Runnable {
        loadTrack(search.text.toString())
    }

    private lateinit var progressBar: ProgressBar

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_TRACK_DEBOUNCE_DELAY)

    }

    private fun clickDebonce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private var textSearch: String = ""
    private lateinit var search: EditText
    private lateinit var noContentPlaceHolder: LinearLayout
    private lateinit var noInternetPlaceHolder: LinearLayout
    private lateinit var recyclerViewTrak: RecyclerView
    private lateinit var trakAdapter: TrackAdapter
    private lateinit var interactorSearchHistory: InteractorSearchHistory
    private lateinit var recyclerViewHistoryTrack: RecyclerView
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var linearLayoutHistory: LinearLayout
    private lateinit var trackInteractor: TrackInteractorApi

    private fun showHistory() {
        linearLayoutHistory.visibility =
            if (interactorSearchHistory.hasHistory()) View.VISIBLE else View.GONE }

    private fun getHisory() {
        historyTrackAdapter = TrackAdapter(
            interactorSearchHistory.getSong().reversed(),
            object : TrackAdapter.OnItemClickListener {
                override fun onItemClick(track: Track) {
                    clickOnTrack(track)

                }
            })

        recyclerViewHistoryTrack.adapter = historyTrackAdapter


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.progressBar)
        linearLayoutHistory = findViewById(R.id.linear_layout_history)
        recyclerViewHistoryTrack = findViewById(R.id.recycler_history_track)
        noContentPlaceHolder = findViewById(R.id.error_no_content)
        noInternetPlaceHolder = findViewById(R.id.error_internet)
        recyclerViewTrak = findViewById(R.id.recycler_track)
        search = findViewById(R.id.search)

        val toolbar: Toolbar = findViewById(R.id.toolbar_search)
        val clearButton: ImageView = findViewById(R.id.clearIcon)

        interactorSearchHistory =
            InteractorSearchHistory(Creator().provideInteractorSearchHistory(this))

        trackInteractor = Creator().provideTracksInteractor()

        recyclerViewHistoryTrack.layoutManager = LinearLayoutManager(this)

        getHisory()
        showHistory()


        val updateBtn = findViewById<Button>(R.id.btn_search_update)


        recyclerViewTrak.layoutManager = LinearLayoutManager(this)

        trakAdapter = TrackAdapter(emptyList(), object : TrackAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                clickOnTrack(track)
            }
        })
        recyclerViewTrak.adapter = trakAdapter


        toolbar.setNavigationOnClickListener {
            finish()
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                if (search.hasFocus() && s?.isEmpty() == true) {
                    noContentPlaceHolder.visibility = View.GONE
                    noInternetPlaceHolder.visibility = View.GONE

                    showHistory()

                    trakAdapter.updateData(emptyList())
                    handler.removeCallbacks(searchRunnable)
                } else {
                    linearLayoutHistory.visibility = View.GONE
                    searchDebounce()
                }
            }
            override fun afterTextChanged(s: Editable?) {
                textSearch = search.getText().toString()
            }
        }
        search.addTextChangedListener(textWatcher)
        search.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && search.text.isEmpty() && interactorSearchHistory.hasHistory()) {
                linearLayoutHistory.visibility = View.VISIBLE
            } else {
                linearLayoutHistory.visibility = View.GONE
            }

        }
        clearButton.setOnClickListener {
            search.setText("")

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)

            trakAdapter.updateData(emptyList())

        }

        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadTrack(search.text.toString())
            }
            false
        }
        updateBtn.setOnClickListener {
            loadTrack(search.text.toString())
        }
        val btnClearHistory = findViewById<Button>(R.id.btn_clear_history)
        btnClearHistory.setOnClickListener {
            interactorSearchHistory.clear()
            historyTrackAdapter.updateData(emptyList())
            linearLayoutHistory.visibility = View.GONE
        }

    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(FIELD_SEARCH).toString()
        search.setText(textSearch)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FIELD_SEARCH, textSearch)
    }

    private fun clickOnTrack(track: Track) {
        if (clickDebonce()) {
            interactorSearchHistory.write(track)
            getHisory()
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_DETAILS, track)
            startActivity(intent)
        }
    }

    private fun ShowProgressBar() {
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        noInternetPlaceHolder.visibility = View.GONE
        noContentPlaceHolder.visibility = View.GONE
    }

    private fun ShowNoInternet() {
        noInternetPlaceHolder.visibility = View.VISIBLE
        noContentPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun ShowNoContent() {
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.VISIBLE
    }

    fun loadTrack(text: String) {

        ShowProgressBar()
        handler.removeCallbacks(searchRunnable)

        trackInteractor.searchTracks(text,
            object : TrackConsumer<List<Track>> {
                override fun consume(data: DataConsumer<List<Track>>) {

                    runOnUiThread {
                        when (data) {
                            is DataConsumer.Success -> {
                                progressBar.visibility = View.GONE
                                recyclerViewTrak.visibility = View.VISIBLE
                                trakAdapter.updateData(data.data)
                            }

                            is DataConsumer.ResponseFailure -> {
                                ShowNoInternet()
                            }

                            is DataConsumer.ResponseNoContent -> {
                                ShowNoContent()
                            }
                        }

                    }

                }

            })
    }

    companion object {
        const val FIELD_SEARCH = "FIELD_SEARCH"

        private const val SEARCH_TRACK_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 2000L
    }
}

