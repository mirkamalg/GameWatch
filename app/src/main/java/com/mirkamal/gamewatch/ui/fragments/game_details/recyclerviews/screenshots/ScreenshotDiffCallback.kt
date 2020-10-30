package com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.screenshots

import androidx.recyclerview.widget.DiffUtil
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO

/**
 * Created by Mirkamal on 30 October 2020
 */
class ScreenshotDiffCallback : DiffUtil.ItemCallback<ScreenshotPOJO>() {
    override fun areItemsTheSame(oldItem: ScreenshotPOJO, newItem: ScreenshotPOJO): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ScreenshotPOJO, newItem: ScreenshotPOJO): Boolean {
        return oldItem == newItem
    }
}