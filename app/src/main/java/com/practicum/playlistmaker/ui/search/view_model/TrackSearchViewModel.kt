package com.practicum.playlistmaker.ui.search.view_model


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.api.TrackInteractorApi
import com.practicum.playlistmaker.domain.consumer.DataConsumer
import com.practicum.playlistmaker.domain.consumer.TrackConsumer
import com.practicum.playlistmaker.domain.interactor.InteractorSearchHistory
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import com.practicum.playlistmaker.ui.search.models.SearchState


class TrackSearchViewModel(
    private val interactorSearchHistory: InteractorSearchHistory, // История поиска
    private val trackInteractor: TrackInteractorApi
) : ViewModel() {

    //private val trakAdapter = TrackAdapter(emptyList(), ::onTrackClicked)
    //private val historyTrackAdapter = TrackAdapter(emptyList(), ::onTrackClicked)

   // fun getTrackAdapter(): TrackAdapter = trakAdapter
    //fun getTrackHistoryAdapter(): TrackAdapter = historyTrackAdapter


    private var isClickAllowed = true

    //private lateinit var interactorSearchHistory: InteractorSearchHistory


    //private lateinit var trackInteractor: TrackInteractorApi

    private var textSearch: String = ""

    private val handler = Handler(Looper.getMainLooper())


    private val _searchState: MutableLiveData<SearchState> = MutableLiveData()
    val searchState: LiveData<SearchState> = _searchState

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks


    private val _historyTracks = MutableLiveData<List<Track>>()
    val historyTracks: LiveData<List<Track>> = _historyTracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /*
    private val _showHistory = MutableLiveData<Boolean>()
    val showHistory: LiveData<Boolean> = _showHistory

    */

    private val _navigateToTrackDetails = SingleLiveEvent<Track>()
    val navigateToTrackDetails: LiveData<Track> = _navigateToTrackDetails

    private val _showBtnClear = MutableLiveData<Boolean>()
    val showBtnClear: LiveData<Boolean> = _showBtnClear


    fun updateReqvest(query: String) {
        Log.d("1111112121","${query}")
        if (query.isEmpty()) {
            Log.d("1111112121","Пусто")
            //тетс
            //_tracks.postValue(emptyList())  // под вопросом
            //_showHistory.postValue(false)
            handler.removeCallbacks(searchRunnable)
            _searchState.postValue(SearchState.ContentHistory)
            _showBtnClear.value =false

        } else {
            Log.d("1111112121","else ${query}")
            _showBtnClear.postValue(true)
            searchDebounce(query)
           // loadTrack(query)
        }
    }


    init {
        updateHistory() // Инициализация истории при создании ViewModel
    }
    //fun onCreate() {
        //interactorSearchHistory =
           // InteractorSearchHistory(Creator.provideInteractorSearchHistory())

        //updateHistory()

        //trackInteractor = Creator.provideTracksInteractor()
    //}

    fun addTextChangedListener() {
       // trakAdapter.updateData(emptyList())
        handler.removeCallbacks(searchRunnable)
    }

    fun onSearchFocusGained() {
        //тетс
        if (interactorSearchHistory.hasHistory()){
            _searchState.postValue(SearchState.ContentHistory)
        }
       // _showHistory.value =
    }


    fun clearSearch() {
        //тетс
       // _tracks.postValue(emptyList())   //новое не понятно
        //_showHistory.postValue(true)
        _searchState.postValue(SearchState.ContentHistory)
        //trakAdapter.updateData(emptyList())
    }

    private var searchRunnable = Runnable {
        loadTrack(textSearch)
    }

    fun searchDebounce(changedText: String) {
        if (textSearch == changedText) {
            return
        }
        textSearch = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_TRACK_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_TRACK_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 2000L
        const val FIELD_SEARCH = "FIELD_SEARCH"

        fun provideFactory(
            interactorSearchHistory: InteractorSearchHistory,
            trackInteractor: TrackInteractorApi
        ) = viewModelFactory {
            initializer {
                TrackSearchViewModel(interactorSearchHistory, trackInteractor)
            }
        }
    }

    fun loadTrack(text: String) {

        textSearch = text
        Log.d("1111112121","loadTrack")
        _searchState.postValue(SearchState.ProgressBar)
        //тест ниже строчку по идеи тогда можно убрать
        //_showHistory.postValue(false)

        handler.removeCallbacks(searchRunnable)

        trackInteractor.searchTracks(text,
            object : TrackConsumer<List<Track>> {
                override fun consume(data: DataConsumer<List<Track>>) {

                    when (data) {

                        is DataConsumer.Success -> {
                           // handler.post {
                                _tracks.postValue(data.data)
                                _searchState.postValue(SearchState.Content)
                                //renderState(SearchState.Content)
                                //trakAdapter.updateData(data.data)
                            //}
                        }

                        is DataConsumer.ResponseFailure -> {
                                _searchState.postValue(SearchState.NoInternet)
                                //renderState(SearchState.NoInternet)
                        }

                        is DataConsumer.ResponseNoContent -> {
                            _searchState.postValue(SearchState.NotContent)

                               // renderState(SearchState.NotContent)

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
    }

    private fun updateHistory() {
        //historyTrackAdapter.updateData(interactorSearchHistory.getSong().reversed())

        val history = interactorSearchHistory.getSong().reversed()
        _historyTracks.postValue(history)
        if (history.isNotEmpty()){
            _searchState.postValue(SearchState.ContentHistory)
        }
        //тест
        //_showHistory.postValue(history.isNotEmpty()) //тут показываем если история есть тест
    }

    fun clearHistory() {
        interactorSearchHistory.clear()
        updateHistory()
        //historyTrackAdapter.updateData(emptyList())
        //interactorSearchHistory.clear()
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

    fun onTrackClicked(track: Track) {
        if (clickDebonce()) {
            interactorSearchHistory.write(track)
            updateHistory()
            _navigateToTrackDetails.postValue(track)
           // _navigateToTrackDetails.value = TrackMapper.mapToTrackAudioPlayer(track)
            //viewState.clickOnTrack(TrackMapper.mapToTrackAudioPlayer(track))
        }
    }


}