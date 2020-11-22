package com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.screenshots

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO

/**
 * Created by Mirkamal on 30 October 2020
 */
class ScreenshotsListAdapter(private val listener: (ScreenshotPOJO, Int) -> Unit) :
    ListAdapter<ScreenshotPOJO, ScreenshotsListViewHolder>(ScreenshotDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.item_screenshots, parent, false)
        return ScreenshotsListViewHolder.from(rootView, listener)
    }

    override fun onBindViewHolder(holder: ScreenshotsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}