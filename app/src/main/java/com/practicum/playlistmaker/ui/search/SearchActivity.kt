package com.practicum.playlistmaker.ui.search


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.presentation.controller.TrackSearchController
import com.practicum.playlistmaker.presentation.controller.TrackSearchHistoryController


const val TRACK_DETAILS = "TRACK_DETAILS"

class SearchActivity : AppCompatActivity() {

    private lateinit var interactorSearchHistory: InteractorSearchHistory
    private lateinit var trackInteractor: TrackInteractorApi

    private lateinit var trackSearchHistoryController: TrackSearchHistoryController
    private lateinit var trackSearchController: TrackSearchController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        interactorSearchHistory =
            InteractorSearchHistory(Creator.provideInteractorSearchHistory())

        trackSearchHistoryController =
            Creator.provideTrackSearchHistoryController(this, interactorSearchHistory)
        trackSearchHistoryController.onCreate()
        trackSearchHistoryController.showHistory()

        trackInteractor = Creator.provideTracksInteractor()

        trackSearchController = Creator.provideTrackSearchController(
            this,
            trackSearchHistoryController
        )

        trackSearchController.onCreate()

        val toolbar: Toolbar = findViewById(R.id.toolbar_search)

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        trackSearchController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        trackSearchController.onSaveInstanceState(outState)
    }

}

