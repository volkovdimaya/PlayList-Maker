package com.practicum.playlistmaker.domain.add_playlist.interactor

import android.net.Uri
import com.practicum.playlistmaker.domain.add_playlist.PicRepository
import com.practicum.playlistmaker.ui.library.add_playlist.api.InteractorSavePic

class InteractorSavePicImpl(private val picRepository: PicRepository) : InteractorSavePic {

    override fun savePic(uri: Uri, fileName: String) {
        picRepository.savePic(uri, fileName)
    }

    override fun getPic(fileName: String): Uri? {
        return picRepository.getPic(fileName)
    }


}
