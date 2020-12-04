package com.mirkamal.gamewatch.ui.fragments.mygames.recyclerviews

import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.databinding.ItemMyGamesBinding
import com.mirkamal.gamewatch.model.entity.Game

/**
 * Created by Mirkamal on 24 October 2020
 */
class MyGamesListViewHolder private constructor(
    private val binding: ItemMyGamesBinding,
    private val listener: (game: Game) -> Unit,
    private val deleteListener: (position: Int, id: Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(game: Game) {
        binding.apply {
            this.game = game
            this.imageViewDelete.setOnClickListener {
                deleteListener(adapterPosition, game.id)
            }
            executePendingBindings()
        }
        itemView.setOnClickListener {
            listener(game)
        }
    }

    companion object {
        fun from(
            binding: ItemMyGamesBinding,
            listener: (game: Game) -> Unit,
            deleteListener: (position: Int, id: Long) -> Unit
        ): MyGamesListViewHolder {
            return MyGamesListViewHolder(binding, listener, deleteListener)
        }
    }
}