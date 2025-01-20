package com.practicum.playlistmaker.ui.setting.activity



import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.setting.view_model.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar: Toolbar = findViewById(R.id.toolbar_setting)


        toolbar.setNavigationOnClickListener {
            finish()
        }

        val shareApp: FrameLayout = findViewById(R.id.share_app)
        shareApp.setOnClickListener {
            viewModel.onShareAppClicked()
        }

        val sendSupport: FrameLayout = findViewById(R.id.send_support)
        sendSupport.setOnClickListener {
            viewModel.onSendSupportClicked()
        }

        val userAgreement: FrameLayout = findViewById(R.id.user_agreement)
        userAgreement.setOnClickListener {
            viewModel.onUserAgreementClicked()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.onThemeSwitched(checked)
        }

        viewModel.isDarkTheme.observe(this) { isDarkTheme ->
            Log.d("12121", "${isDarkTheme}")
            themeSwitcher.isChecked = isDarkTheme
        }
    }

}