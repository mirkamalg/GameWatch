package com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.diffutilcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.mirkamal.gamewatch.model.pojo.GamePOJO

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGameDiffCallback: DiffUtil.ItemCallback<GamePOJO>() {
    override fun areItemsTheSame(oldItem: GamePOJO, newItem: GamePOJO): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: GamePOJO, newItem: GamePOJO): Boolean {
        return oldItem == newItem
    }
}