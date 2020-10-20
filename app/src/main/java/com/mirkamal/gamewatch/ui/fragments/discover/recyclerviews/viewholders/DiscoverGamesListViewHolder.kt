package com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.databinding.ItemDiscoverGamesBinding
import com.mirkamal.gamewatch.model.entity.Game

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesListViewHolder private constructor(private val binding: ItemDiscoverGamesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(game: Game) {
        binding.apply {
            this.game = game
            executePendingBindings()
        }

        //Make textview selected for marquee
        binding.textViewGameName.isSelected = true
    }

    companion object {
        fun from(binding: ItemDiscoverGamesBinding): DiscoverGamesListViewHolder {
            return DiscoverGamesListViewHolder(binding)
        }
    }

}