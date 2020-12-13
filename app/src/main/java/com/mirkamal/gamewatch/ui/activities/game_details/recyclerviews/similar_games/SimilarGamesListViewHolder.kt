package com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.similar_games

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.utils.loadImage

/**
 * Created by Mirkamal on 15 November 2020
 */
class SimilarGamesListViewHolder private constructor(itemView: View, private val listener: (Game) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(game: Game) {
        itemView.findViewById<ImageView>(R.id.imageViewGameCover).loadImage(game.coverURL)
        val gameName = itemView.findViewById<TextView>(R.id.textViewGameName)
        gameName.text = game.name
        gameName.isSelected = true

        itemView.setOnClickListener {
            listener(game)
        }
    }

    companion object {
        fun from(itemView: View, listener: (Game) -> Unit): SimilarGamesListViewHolder {
            return SimilarGamesListViewHolder(itemView, listener)
        }
    }
}