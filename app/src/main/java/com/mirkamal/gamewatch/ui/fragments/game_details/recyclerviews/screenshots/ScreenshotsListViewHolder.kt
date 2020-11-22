package com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.screenshots

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO
import com.mirkamal.gamewatch.utils.loadImage

/**
 * Created by Mirkamal on 30 October 2020
 */
class ScreenshotsListViewHolder private constructor(itemView: View, private val listener: (ScreenshotPOJO, Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bind(screenshotPOJO: ScreenshotPOJO) {
        val url = "https:" + screenshotPOJO.url
        val formattedUrl = url.replace("t_thumb", "t_screenshot_med")
        Log.i("ScreenshotsViewHolder", "Loading image: $formattedUrl")
        itemView.findViewById<ImageView>(R.id.imageViewScreenshot).loadImage(formattedUrl)

        itemView.setOnClickListener {
            listener(screenshotPOJO, adapterPosition)
        }
    }

    companion object {
        fun from(itemView: View, listener: (ScreenshotPOJO, Int) -> Unit): ScreenshotsListViewHolder {
            return ScreenshotsListViewHolder(itemView, listener)
        }
    }
}