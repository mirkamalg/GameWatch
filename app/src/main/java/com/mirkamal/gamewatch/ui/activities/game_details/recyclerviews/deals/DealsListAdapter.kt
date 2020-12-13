package com.mirkamal.gamewatch.ui.activities.game_details.recyclerviews.deals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.pojo.GameDealPOJO
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.deals.DealsListViewHolder
import com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.deals.GameDealDiffCallback

/**
 * Created by Mirkamal on 02 November 2020
 */
class DealsListAdapter : ListAdapter<GameDealPOJO, DealsListViewHolder>(GameDealDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.item_game_deals, parent, false)
        return DealsListViewHolder.from(rootView)
    }

    override fun onBindViewHolder(holder: DealsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}