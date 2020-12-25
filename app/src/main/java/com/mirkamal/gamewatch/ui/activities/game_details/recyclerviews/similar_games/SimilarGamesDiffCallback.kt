package com.mirkamal.gamewatch.ui.activities.game_details.recyclerviews.similar_games

import androidx.recyclerview.widget.DiffUtil
import com.mirkamal.gamewatch.model.parcel.Game

/**
 * Created by Mirkamal on 15 November 2020
 */
class SimilarGamesDiffCallback: DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}