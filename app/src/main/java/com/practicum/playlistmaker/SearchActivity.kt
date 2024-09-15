package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var textSearch: String = ""
    private lateinit var search: EditText

    private val service = ApiRetrofit.getClient().create(SearchTrackApi::class.java)
    private lateinit var noContentPlaceHolder: LinearLayout
    private lateinit var noInternetPlaceHolder: LinearLayout
    private lateinit var recyclerViewTrak: RecyclerView
    private lateinit var trakAdapter: AdapterTrack

    lateinit var songs: List<Track>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val updateBtn = findViewById<Button>(R.id.btn_search_update)
        noContentPlaceHolder = findViewById(R.id.error_no_content)
        noInternetPlaceHolder = findViewById(R.id.error_internet)


        recyclerViewTrak = findViewById(R.id.recycler_track)
        recyclerViewTrak.layoutManager = LinearLayoutManager(this)

        trakAdapter = AdapterTrack(emptyList())
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
            }

            override fun afterTextChanged(s: Editable?) {
                textSearch = search.getText().toString()
            }
        }
        search.addTextChangedListener(textWatcher)

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

    //onSaveInstanceState и onRestoreInstanceState не вызывается но данные сохраняются при полвороте экрана
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FIELD_SEARCH, textSearch)
    }

    companion object {
        const val FIELD_SEARCH = "FIELD_SEARCH"
    }


    fun loadTrack(text: String) {
        service.search(text)
            .enqueue(object : Callback<SearchTrackResponse> {
                override fun onResponse(
                    call: Call<SearchTrackResponse>,
                    response: Response<SearchTrackResponse>
                ) {
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
                                artworkUrl100 = track.artworkUrl100 ?: ""
                            )
                        }
                        trakAdapter.updateData(songs)
                    }


                }

                override fun onFailure(call: Call<SearchTrackResponse>, t: Throwable) {
                    noInternetPlaceHolder.visibility = View.VISIBLE
                    noContentPlaceHolder.visibility = View.GONE
                    recyclerViewTrak.visibility = View.GONE
                }

            })
    }
}

