package com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.similar_games

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.entity.Game

/**
 * Created by Mirkamal on 15 November 2020
 */
class SimilarGamesListAdapter: ListAdapter<Game, SimilarGamesListViewHolder>(SimilarGamesDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarGamesListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_similar_games, parent, false)
        return SimilarGamesListViewHolder.from(itemView)
    }

    override fun onBindViewHolder(holder: SimilarGamesListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}