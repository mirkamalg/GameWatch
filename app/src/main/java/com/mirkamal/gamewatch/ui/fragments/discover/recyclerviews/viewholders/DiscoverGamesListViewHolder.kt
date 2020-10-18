package com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.databinding.ItemDiscoverGamesBinding
import com.mirkamal.gamewatch.model.pojo.GamePOJO

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesListViewHolder private constructor(private val binding: ItemDiscoverGamesBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(gamePOJO: GamePOJO) {
        binding.apply {
            this.game = gamePOJO
            executePendingBindings()
        }
    }

    companion object {
        fun from(binding: ItemDiscoverGamesBinding) : DiscoverGamesListViewHolder {
            return DiscoverGamesListViewHolder(binding)
        }
    }

}