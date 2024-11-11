package com.practicum.playlistmaker.presentation.controller

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.SearchActivity
import com.practicum.playlistmaker.ui.search.TrackAdapter


class TrackSearchController(
    private val activity: Activity,
    private val trakAdapter: TrackAdapter,
    private val trackSearchHistoryController: TrackSearchHistoryController
) {

    private lateinit var trackInteractor: TrackInteractorApi // не инициализирован

    private var textSearch: String = ""

    private val handler = Handler(Looper.getMainLooper())


    private lateinit var noContentPlaceHolder: LinearLayout //
    private lateinit var noInternetPlaceHolder: LinearLayout //
    private lateinit var recyclerViewTrak: RecyclerView //
    private lateinit var progressBar: ProgressBar //
    private lateinit var search: EditText // вроде все

    fun onCreate() {
        progressBar = activity.findViewById(R.id.progressBar)

        noContentPlaceHolder = activity.findViewById(R.id.error_no_content)
        noInternetPlaceHolder = activity.findViewById(R.id.error_internet)
        recyclerViewTrak = activity.findViewById(R.id.recycler_track)
        search = activity.findViewById(R.id.search)

        val clearButton: ImageView = activity.findViewById(R.id.clearIcon)
        val updateBtn = activity.findViewById<Button>(R.id.btn_search_update)

        recyclerViewTrak.layoutManager = LinearLayoutManager(activity)

        recyclerViewTrak.adapter = trakAdapter


        //изменить чтобы показывать историю
        search.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            clearButton.isVisible = (!s.isNullOrEmpty())

            if (search.hasFocus() && s?.isEmpty() == true) {
                progressBar.visibility = View.GONE
                noContentPlaceHolder.visibility = View.GONE
                noInternetPlaceHolder.visibility = View.GONE

                trackSearchHistoryController.showHistory()

                trakAdapter.updateData(emptyList())
                handler.removeCallbacks(searchRunnable)
            } else {
                trackSearchHistoryController.hiddenHistory()
                searchDebounce()
            }
        },
            afterTextChanged = {
                textSearch = search.getText().toString()
            }
        )

        search.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && search.text.isEmpty()){
                trackSearchHistoryController.showHistory()
            }
        }

        clearButton.setOnClickListener {
            search.setText("")

            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
    }


    private var searchRunnable = Runnable {
        loadTrack(search.text.toString())
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_TRACK_DEBOUNCE_DELAY)

    }

    companion object {

        private const val SEARCH_TRACK_DEBOUNCE_DELAY = 2000L
    }

    fun loadTrack(text: String) {

        showProgressBar()
        handler.removeCallbacks(searchRunnable)

        trackInteractor.searchTracks(text,
            object : TrackConsumer<List<Track>> {
                override fun consume(data: DataConsumer<List<Track>>) {
                    activity.runOnUiThread {
                        when (data) {
                            is DataConsumer.Success -> {
                                progressBar.visibility = View.GONE
                                recyclerViewTrak.visibility = View.VISIBLE
                                trakAdapter.updateData(data.data)
                            }

                            is DataConsumer.ResponseFailure -> {
                                showNoInternet()
                            }

                            is DataConsumer.ResponseNoContent -> {
                                showNoContent()
                            }
                        }
                    }
                }

            })
    }

    private fun showProgressBar() {
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        noInternetPlaceHolder.visibility = View.GONE
        noContentPlaceHolder.visibility = View.GONE
    }

    private fun showNoInternet() {
        noInternetPlaceHolder.visibility = View.VISIBLE
        noContentPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showNoContent() {
        noInternetPlaceHolder.visibility = View.GONE
        recyclerViewTrak.visibility = View.GONE
        noContentPlaceHolder.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SearchActivity.FIELD_SEARCH, textSearch)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        textSearch = savedInstanceState.getString(SearchActivity.FIELD_SEARCH).toString()
        search.setText(textSearch)
    }
}