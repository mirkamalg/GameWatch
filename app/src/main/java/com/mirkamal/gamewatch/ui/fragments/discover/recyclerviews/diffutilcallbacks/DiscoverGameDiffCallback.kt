package com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.diffutilcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.mirkamal.gamewatch.model.entity.Game

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGameDiffCallback: DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}