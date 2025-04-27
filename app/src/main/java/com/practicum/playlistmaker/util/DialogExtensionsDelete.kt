package com.practicum.playlistmaker.util

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R

fun Fragment.showConfirmDeleteDialog(onConfirm: () -> Unit, message: String, title: String) {
    MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertExitDialog)
        .setView(
            LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL

                // Добавляем первый элемент
                addView(createTitleView(requireContext(), title))

                // Добавляем второй элемент
                addView(createMessageView(requireContext(), message))
            }
        )
        .setPositiveButton(R.string.yes) { _, _ ->
            onConfirm()
        }
        .setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}

private  fun createMessageView(requireContext: Context, message: String): View {
    return TextView(requireContext).apply {
        text = message
        setTextColor(
            ContextCompat.getColor(
                requireContext,
                R.color.message_dialog
            )
        )
        textSize = 14f
        setPadding(
            resources.getDimensionPixelSize(R.dimen.dialog_padding_start),
            resources.getDimensionPixelSize(R.dimen.dialog_padding_top),
            resources.getDimensionPixelSize(R.dimen.dialog_padding_end),
            resources.getDimensionPixelSize(R.dimen.dialog_padding_bottom)
        )
        gravity = Gravity.START
    }
}
private  fun createTitleView(requireContext: Context, title: String): View {
    return TextView(requireContext).apply {
        text = title
        setTextColor(
            ContextCompat.getColor(
                requireContext,
                R.color.message_dialog
            )
        )
        textSize = 16f
        setPadding(
            resources.getDimensionPixelSize(R.dimen.dialog_padding_start),
            resources.getDimensionPixelSize(R.dimen.dialog_padding_top),
            resources.getDimensionPixelSize(R.dimen.dialog_padding_end),
            resources.getDimensionPixelSize(R.dimen.dialog_padding_bottom)
        )
        gravity = Gravity.START
    }
}
