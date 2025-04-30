package com.practicum.playlistmaker.ui.library.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.share_data.SharedTrackViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DialogDeleteTrackInPlaylistFragment : DialogFragment() {

    private val sharedTrackViewModel: SharedTrackViewModel by activityViewModel()

    interface OnTrackDeleteListener {
        fun onTrackConfirmed(track: Track)
    }
    private var track: Track? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {



        val context = requireContext()
        val messageView = TextView(context).apply {
            text = "Хотите удалить трек?"
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.message_dialog
                )
            )
            textSize = 14f
            setPadding(24, 23, 8, 0)
            gravity = Gravity.START
        }


        return MaterialAlertDialogBuilder(context, R.style.MyAlertDialog)
            .setView(messageView)
            .setPositiveButton(R.string.yes) { _, _ ->
                lifecycleScope.launch {
                    track = sharedTrackViewModel.trackFlow.first()
                    (parentFragment as? OnTrackDeleteListener)?.onTrackConfirmed(track!!)
                }

            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}