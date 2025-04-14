package com.practicum.playlistmaker.data.add_playlist


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.domain.add_playlist.PicRepository
import java.io.File
import java.io.FileOutputStream

class PicRepositoryImpl(val context : Context) : PicRepository {
    override fun savePic(uri: Uri, fileName: String) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")


        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(filePath, fileName)

        val inputStream = context.contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)




//        context.contentResolver.openInputStream(uri)?.use { inputStream ->
//            FileOutputStream(file).use { outputStream ->
//                BitmapFactory.decodeStream(inputStream)
//                    ?.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
//            }
//        }
    }

    override fun getPic(fileName: String): Uri? {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, fileName)

        return file.toUri() ?: null
    }
}