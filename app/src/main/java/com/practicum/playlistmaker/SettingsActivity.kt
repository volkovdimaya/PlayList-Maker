package com.practicum.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar: Toolbar = findViewById(R.id.toolbar_setting)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val share_app: FrameLayout = findViewById(R.id.share_app)
        share_app.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_course))
            startActivity(sendIntent)
        }

        val send_support: FrameLayout = findViewById(R.id.send_support)
        send_support.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_developer)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_developer_theme));
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                arrayOf(getString(R.string.send_developer_body))
            )
            startActivity(shareIntent)
        }

        val user_agreement: FrameLayout = findViewById(R.id.user_agreement)
        user_agreement.setOnClickListener {
            val view = Intent(Intent.ACTION_VIEW)
            view.data = Uri.parse(getString(R.string.link_user_agreement))
            startActivity(view)
        }
    }

}