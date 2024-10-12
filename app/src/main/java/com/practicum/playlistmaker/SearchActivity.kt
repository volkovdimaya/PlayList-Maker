package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val TRACK_DETAILS = "TRACK_DETAILS"
class SearchActivity : AppCompatActivity() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable = Runnable { searchDebounce() }

    private lateinit var progressBar : ProgressBar

    private fun searchDebounce()
    {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_TRACK_DEBOUNCE_DELAY)
        loadTrack(search.text.toString())
    }

    private fun clickDebonce() : Boolean
    {
        val current = isClickAllowed
        if (isClickAllowed)
        {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private var textSearch: String = ""
    private lateinit var search: EditText

    private val service = ApiRetrofit.getClient().create(SearchTrackApi::class.java)
    private lateinit var noContentPlaceHolder: LinearLayout
    private lateinit var noInternetPlaceHolder: LinearLayout
    private lateinit var recyclerViewTrak: RecyclerView
    private lateinit var trakAdapter: TrackAdapter

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var recyclerViewHistoryTrack: RecyclerView
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.progressBar)

        val linearLayoutHistory = findViewById<LinearLayout>(R.id.linear_layout_history)

        sharedPrefs = getSharedPreferences(com.practicum.playlistmaker.PLAYLIST_MAKER, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        searchHistory.read()

        recyclerViewHistoryTrack = findViewById(R.id.recycler_history_track)

        recyclerViewHistoryTrack.layoutManager = LinearLayoutManager(this)
        historyTrackAdapter = TrackAdapter(
            searchHistory.getSong().reversed(),
            object : TrackAdapter.OnItemClickListener {
                override fun onItemClick(track: Track) {
                    clickOnTrack(track)

                }
            })
        recyclerViewHistoryTrack.adapter = historyTrackAdapter
        linearLayoutHistory.visibility = if (searchHistory.hasHistory) View.VISIBLE else View.GONE

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == HISTORY_LIST_TRACK) {
                searchHistory.read()
                if (searchHistory.hasHistory) {
                    historyTrackAdapter.updateDataHistory(searchHistory.getSong())
                    recyclerViewHistoryTrack.scrollToPosition(0)

                }
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)

        val updateBtn = findViewById<Button>(R.id.btn_search_update)
        noContentPlaceHolder = findViewById(R.id.error_no_content)
        noInternetPlaceHolder = findViewById(R.id.error_internet)



        recyclerViewTrak = findViewById(R.id.recycler_track)
        recyclerViewTrak.layoutManager = LinearLayoutManager(this)

        trakAdapter = TrackAdapter(emptyList(), object : TrackAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                clickOnTrack(track)
            }
        })
        recyclerViewTrak.adapter = trakAdapter


        search = findViewById(R.id.search)
        val toolbar: Toolbar = findViewById(R.id.toolbar_search)
        val clearButton: ImageView = findViewById(R.id.clearIcon)

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

                    linearLayoutHistory.visibility = View.VISIBLE
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
            if (hasFocus && search.text.isEmpty() && searchHistory.hasHistory) {
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
                true
            }
            false
        }
        updateBtn.setOnClickListener {
            loadTrack(search.text.toString())
        }
        val btnClearHistory = findViewById<Button>(R.id.btn_clear_history)
        btnClearHistory.setOnClickListener {
            searchHistory.clear()
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

    companion object {
        const val FIELD_SEARCH = "FIELD_SEARCH"

        private const val SEARCH_TRACK_DEBOUNCE_DELAY = 2000L
        private  const val CLICK_DEBOUNCE_DELAY = 2000L
    }

    private fun clickOnTrack(track: Track) {
        val searchHistory = SearchHistory(sharedPrefs)
        searchHistory.read()
        searchHistory.update(track)
        if (clickDebonce())
        {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_DETAILS, track)
            startActivity(intent)
        }
    }

    fun loadTrack(text: String) {
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        handler.removeCallbacks(searchRunnable)


        noInternetPlaceHolder.visibility = View.GONE
        noContentPlaceHolder.visibility = View.GONE

        service.search(text)
            .enqueue(object : Callback<SearchTrackResponse> {
                override fun onResponse(
                    call: Call<SearchTrackResponse>,
                    response: Response<SearchTrackResponse>
                ) {
                    progressBar.visibility = View.GONE
                    recyclerViewTrak.visibility = View.VISIBLE
                    val results = response.body()?.results
                    if (results.isNullOrEmpty()) {

                        noInternetPlaceHolder.visibility = View.GONE
                        recyclerViewTrak.visibility = View.GONE
                        noContentPlaceHolder.visibility = View.VISIBLE
                    } else {
                        noInternetPlaceHolder.visibility = View.GONE
                        noContentPlaceHolder.visibility = View.GONE
                        recyclerViewTrak.visibility = View.VISIBLE

                        val songs = results.map { track ->
                            Track(
                                trackName = track.trackName ?: getString(R.string.unknown),
                                artistName = track.artistName ?: getString(R.string.unknown),
                                trackTimeMillis = track.trackTimeMillis ?: 0L,
                                artworkUrl100 = track.artworkUrl100 ?: "",
                                trackId = track.trackId ?: "",
                                releaseDate = track.releaseDate ?: "",
                                primaryGenreName = track.primaryGenreName ?: "",
                                collectionName = track.collectionName ?: "",
                                country = track.country ?: "",
                                previewUrl = track.previewUrl ?: "",
                                )
                        }

                        trakAdapter.updateData(songs)
                    }


                }

                override fun onFailure(call: Call<SearchTrackResponse>, t: Throwable) {
                    noInternetPlaceHolder.visibility = View.VISIBLE
                    noContentPlaceHolder.visibility = View.GONE
                    recyclerViewTrak.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }

            })
    }

    override fun onStop() {
        super.onStop()
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}

