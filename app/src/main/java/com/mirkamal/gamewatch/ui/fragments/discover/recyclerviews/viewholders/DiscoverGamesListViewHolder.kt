package com.mirkamal.gamewatch.ui.fragments.discover.recyclerviews.viewholders

import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.databinding.ItemDiscoverGamesBinding
import com.mirkamal.gamewatch.model.parcel.Game
import com.mirkamal.gamewatch.utils.NINTENDO_SWITCH_ID
import com.mirkamal.gamewatch.utils.PC_ID
import com.mirkamal.gamewatch.utils.PLAYSTATION_IDs
import com.mirkamal.gamewatch.utils.XBOX_IDs

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesListViewHolder private constructor(
    private val binding: ItemDiscoverGamesBinding,
    private val listener: (Game) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(game: Game, menuListener: (Game, Int) -> Unit) {
        binding.apply {
            this.game = game
            executePendingBindings()
        }

        //Make textview selected for marquee
        binding.textViewGameName.isSelected = true

        configurePlatformIcons(game)
        itemView.setOnClickListener {
            listener(game)
        }

        itemView.findViewById<ImageButton>(R.id.buttonMoreDiscoverGamesItem)
            .setOnClickListener { buttonMore ->
                val popUp = PopupMenu(buttonMore.context, buttonMore)
                val inflater = popUp.menuInflater
                inflater.inflate(R.menu.discover_games_item_menu, popUp.menu)

                popUp.setOnMenuItemClickListener {
                    menuListener(game, it.itemId)
                    true
                }
                popUp.show()
            }
    }

    private fun configurePlatformIcons(game: Game) {
        for (id in game.platforms) {
            if (id == PC_ID.toLong()) binding.imageViewPC.isVisible = true
            if (XBOX_IDs.contains(id.toInt())) binding.imageViewXBOX.isVisible = true
            if (PLAYSTATION_IDs.contains(id.toInt())) binding.imageViewPS.isVisible = true
            if (id == NINTENDO_SWITCH_ID.toLong()) binding.imageViewNintendo.isVisible = true
        }
    }

    companion object {
        fun from(
            binding: ItemDiscoverGamesBinding,
            listener: (Game) -> Unit
        ): DiscoverGamesListViewHolder {
            return DiscoverGamesListViewHolder(binding, listener)
        }
    }

}