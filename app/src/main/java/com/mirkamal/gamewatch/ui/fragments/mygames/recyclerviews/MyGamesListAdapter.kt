package com.mirkamal.gamewatch.ui.fragments.mygames.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mirkamal.gamewatch.databinding.ItemMyGamesBinding
import com.mirkamal.gamewatch.model.parcel.Game

/**
 * Created by Mirkamal on 24 October 2020
 */
class MyGamesListAdapter(
    private val listener: (game: Game) -> Unit
) : ListAdapter<Game, MyGamesListViewHolder>(MyGamesDiffCallback()) {

    lateinit var menuListener: (Int, Game, Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGamesListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMyGamesBinding.inflate(layoutInflater, parent, false)
        return MyGamesListViewHolder.from(binding, listener, menuListener)
    }

    override fun onBindViewHolder(holder: MyGamesListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}