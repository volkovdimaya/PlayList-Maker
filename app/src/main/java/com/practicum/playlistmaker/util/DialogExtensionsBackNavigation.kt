package com.practicum.playlistmaker.util

import android.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R

fun Fragment.showConfirmExitDialog(onConfirm: () -> Unit) {
    MaterialAlertDialogBuilder(requireContext(),  R.style.MyAlertDialog)
        .setTitle(getString(R.string.title_complite_playlist))
        .setMessage(getString(R.string.message_data_lose))
        .setNeutralButton(getString(R.string.cancle)) { dialog, _ -> dialog.dismiss() }
        .setPositiveButton(getString(R.string.exit)) { _, _ -> onConfirm() }
        .create()
        .apply {
            setOnShowListener {
                getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.search_item_title)
                )
                getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.search_item_title)
                )
            }
        }
        .show()
}