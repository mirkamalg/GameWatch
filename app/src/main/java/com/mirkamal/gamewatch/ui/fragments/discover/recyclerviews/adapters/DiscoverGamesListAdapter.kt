package com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamal.gamewatch.databinding.ItemDiscoverGamesBinding
import com.mirkamal.gamewatch.model.pojo.GamePOJO
import com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.diffutilcallbacks.DiscoverGameDiffCallback
import com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.viewholders.DiscoverGamesListViewHolder

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesListAdapter: ListAdapter<GamePOJO, DiscoverGamesListViewHolder>(DiscoverGameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverGamesListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDiscoverGamesBinding.inflate(layoutInflater, parent, false)
        return DiscoverGamesListViewHolder.from(binding)
    }

    override fun onBindViewHolder(holder: DiscoverGamesListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}