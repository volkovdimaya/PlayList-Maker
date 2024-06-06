package com.practicum.playlistmaker

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {

    private var textSearch : String = ""
    private lateinit var search : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search = findViewById(R.id.search)
        val toolbar: Toolbar = findViewById(R.id.toolbar_search)
        val clearButton : ImageView = findViewById(R.id.clearIcon)

        toolbar.setNavigationOnClickListener {
            finish()
        }


        val textWatcher = object : TextWatcher{
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

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(`it`.windowToken, 0)

        }

        Toast.makeText(this, "$savedInstanceState Данные загружены", Toast.LENGTH_SHORT).show()
        savedInstanceState?.let {
            textSearch = it.getString(FIELD_SEARCH).orEmpty()
            search.setText(textSearch)
            Toast.makeText(this, "Данные загружены", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(FIELD_SEARCH).toString()

        search.setText(textSearch)

        Toast.makeText(this, "Данные загружены", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onRestoreInstanceState: Данные загружены")
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
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onSaveInstanceState: Данные сохранены")
        search.setText("1111")
    }

    companion object{
        const val FIELD_SEARCH = "FIELD_SEARCH"
    }
}