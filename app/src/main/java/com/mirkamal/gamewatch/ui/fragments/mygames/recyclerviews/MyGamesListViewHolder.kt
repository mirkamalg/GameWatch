package com.mirkamal.gamewatch.ui.fragments.mygames.recyclerviews

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.databinding.ItemMyGamesBinding
import com.mirkamal.gamewatch.model.parcel.Game

/**
 * Created by Mirkamal on 24 October 2020
 */
class MyGamesListViewHolder private constructor(
    private val binding: ItemMyGamesBinding,
    private val listener: (game: Game) -> Unit,
    private val menuListener: (Int, Game, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(game: Game) {
        binding.apply {
            this.game = game
            this.buttonMoreMyGamesItem.setOnClickListener { buttonMore ->
                val popUp = PopupMenu(buttonMore.context, buttonMore)
                val inflater = popUp.menuInflater
                inflater.inflate(R.menu.my_games_item_menu, popUp.menu)

                popUp.setOnMenuItemClickListener {
                    menuListener(adapterPosition, game, it.itemId)
                    true
                }
                popUp.show()
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
            deleteListener: (Int, Game, Int) -> Unit
        ): MyGamesListViewHolder {
            return MyGamesListViewHolder(binding, listener, deleteListener)
        }
    }
}