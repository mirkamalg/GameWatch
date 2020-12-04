package com.mirkamal.gamewatch.utils.libs

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.mirkamal.gamewatch.model.entity.Game
import java.io.ByteArrayOutputStream


/**
 * Created by Mirkamal on 04 December 2020
 */
object MessageGenerator {

    fun generateShareGameText(game: Game): String {
        return "Check this game out! I found it using GameWatch App for Android.\n\n${game.name}\n\n${game.summary}\n\nFor more information:\n${game.url}"
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
}