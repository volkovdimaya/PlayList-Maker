package com.practicum.playlistmaker.presentation.search


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.mapper.TrackMapper
import com.practicum.playlistmaker.ui.search.TrackAdapter


class TrackSearchPresenter(
    private val view: SearchView
) {

    private val trakAdapter = TrackAdapter(emptyList(), ::onTrackClicked)
    private val historyTrackAdapter = TrackAdapter(emptyList(), ::onTrackClicked)

    fun getTrackAdapter(): TrackAdapter = trakAdapter
    fun getTrackHistoryAdapter(): TrackAdapter = historyTrackAdapter


    private var isClickAllowed = true

    private lateinit var interactorSearchHistory: InteractorSearchHistory


    private lateinit var trackInteractor: TrackInteractorApi

    private var textSearch: String = ""

    private val handler = Handler(Looper.getMainLooper())


    fun onCreate() {
        interactorSearchHistory =
            InteractorSearchHistory(Creator.provideInteractorSearchHistory())

        updateHistory()

        trackInteractor = Creator.provideTracksInteractor()
    }

    fun addTextChangedListener() {
        //trackSearchHistoryPresenter.showHistory()
        trakAdapter.updateData(emptyList())
        handler.removeCallbacks(searchRunnable)
    }

    fun afterTextChanged(text: String) {
        textSearch = text
        //тут изменения
    }

    fun clearSearch() {
        trakAdapter.updateData(emptyList())
    }

    private var searchRunnable = Runnable {
        loadTrack(textSearch)
    }

    fun searchDebounce(changedText : String) {
        textSearch =  changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_TRACK_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_TRACK_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 2000L
        const val FIELD_SEARCH = "FIELD_SEARCH"
    }

    fun loadTrack(text: String) {

        view.showProgressBar()
        handler.removeCallbacks(searchRunnable)

        trackInteractor.searchTracks(text,
            object : TrackConsumer<List<Track>> {
                override fun consume(data: DataConsumer<List<Track>>) {
                    when (data) {
                        is DataConsumer.Success -> {
                            handler.post {
                                view.showTracks()
                                trakAdapter.updateData(data.data)
                            }
                        }

                        is DataConsumer.ResponseFailure -> {
                            handler.post {
                                view.showNoInternet()
                            }
                        }

                        is DataConsumer.ResponseNoContent -> {
                            handler.post {
                                view.showNoContent()
                            }
                        }
                    }
                }

            })
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putString(FIELD_SEARCH, textSearch)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        textSearch = savedInstanceState.getString(FIELD_SEARCH).toString()
        // search.setText(textSearch)
    }

    fun updateHistory() {
        historyTrackAdapter.updateData(interactorSearchHistory.getSong().reversed())
    }

    fun clearHistory() {
        historyTrackAdapter.updateData(emptyList())
        interactorSearchHistory.clear()
    }

    private fun clickDebonce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    fun showHistory(): Boolean {
        return interactorSearchHistory.hasHistory()
    }

    private fun onTrackClicked(track: Track) {
        if (clickDebonce()) {
            interactorSearchHistory.write(track)
            updateHistory()
            view.clickOnTrack(TrackMapper.mapToTrackAudioPlayer(track))
        }
    }


}