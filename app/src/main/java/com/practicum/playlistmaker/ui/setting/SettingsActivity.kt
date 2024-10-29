package com.practicum.playlistmaker.ui.setting


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar: Toolbar = findViewById(R.id.toolbar_setting)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val shareApp: FrameLayout = findViewById(R.id.share_app)
        shareApp.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_course))
            startActivity(sendIntent)
        }

        val sendSupport: FrameLayout = findViewById(R.id.send_support)
        sendSupport.setOnClickListener {
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

        val userAgreement: FrameLayout = findViewById(R.id.user_agreement)
        userAgreement.setOnClickListener {
            val view = Intent(Intent.ACTION_VIEW)
            view.data = Uri.parse(getString(R.string.link_user_agreement))
            startActivity(view)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)


        val interactorTheme = Creator().provideInteractorTheme(this)


        themeSwitcher.setChecked(interactorTheme.isDarkTheme())

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            interactorTheme.switchTheme(checked)
        }
    }

}