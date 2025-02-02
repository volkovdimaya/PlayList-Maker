package com.practicum.playlistmaker.ui.setting.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.setting.view_model.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.shareApp.setOnClickListener {
            viewModel.onShareAppClicked()
        }

        binding.sendSupport.setOnClickListener {
            viewModel.onSendSupportClicked()
        }
        binding.userAgreement.setOnClickListener {
            viewModel.onUserAgreementClicked()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.onThemeSwitched(checked)
        }

        viewModel.isDarkTheme.observe(viewLifecycleOwner) { isDarkTheme ->
            binding.themeSwitcher.isChecked = isDarkTheme
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}