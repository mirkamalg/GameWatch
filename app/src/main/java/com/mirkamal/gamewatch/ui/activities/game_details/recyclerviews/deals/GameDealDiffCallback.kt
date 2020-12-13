package com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.deals

import androidx.recyclerview.widget.DiffUtil
import com.mirkamal.gamewatch.model.pojo.GameDealPOJO

/**
 * Created by Mirkamal on 02 November 2020
 */
class GameDealDiffCallback: DiffUtil.ItemCallback<GameDealPOJO>() {
    override fun areItemsTheSame(oldItem: GameDealPOJO, newItem: GameDealPOJO): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: GameDealPOJO, newItem: GameDealPOJO): Boolean {
        return oldItem == newItem
    }
}