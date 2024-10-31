package com.practicum.playlistmaker.presentation.controller

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.practicum.playlistmaker.ui.search.TRACK_DETAILS
import com.practicum.playlistmaker.ui.search.TrackAdapter

class TrackSearchHistoryController(
    private val activity: Activity,
    private var historyTrackAdapter: TrackAdapter
) {

    private lateinit var linearLayoutHistory: LinearLayout
    private lateinit var recyclerViewHistoryTrack: RecyclerView
    fun onCreate() {
        linearLayoutHistory = activity.findViewById(R.id.linear_layout_history)
        recyclerViewHistoryTrack = activity.findViewById(R.id.recycler_history_track)

        recyclerViewHistoryTrack.layoutManager = LinearLayoutManager(activity)

        val btnClearHistory = activity.findViewById<Button>(R.id.btn_clear_history)
        btnClearHistory.setOnClickListener {
            interactorSearchHistory.clear()
            historyTrackAdapter.updateData(emptyList())
            linearLayoutHistory.visibility = View.GONE
        }
    }

    fun hiddenHistory() {
        linearLayoutHistory.visibility = View.GONE
    }

    private lateinit var interactorSearchHistory: InteractorSearchHistory


    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())


    private fun getHisory() {
        historyTrackAdapter = TrackAdapter(
            interactorSearchHistory.getSong().reversed(), ::clickOnTrack
        )
        recyclerViewHistoryTrack.adapter = historyTrackAdapter
    }

    private fun clickOnTrack(track: Track) {
        if (clickDebonce()) {
            interactorSearchHistory.write(track)
            getHisory()
            val intent = Intent(activity, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_DETAILS, track)
            activity.startActivity(intent)
        }
    }

    private fun clickDebonce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun showHistory() {
        linearLayoutHistory.isVisible = interactorSearchHistory.hasHistory()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 2000L
    }
}