package com.practicum.playlistmaker.ui.audioplayer.models

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val buttonText : String, val progress: String) {
    class Default : PlayerState(false, "Play", "00:00")
    class Prepared : PlayerState(false, "Play", "00:00")
    class Playing(progress: String) : PlayerState(true, "Pause", progress )
    class Paused(progress: String) : PlayerState(true, "Play", progress)

}