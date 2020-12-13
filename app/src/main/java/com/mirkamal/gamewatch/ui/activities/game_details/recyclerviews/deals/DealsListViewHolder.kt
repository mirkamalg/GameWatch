package com.mirkamal.gamewatch.ui.fragments.game_details.recyclerviews.deals

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.pojo.GameDealPOJO
import com.mirkamal.gamewatch.utils.loadImage

/**
 * Created by Mirkamal on 02 November 2020
 */
class DealsListViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetTextI18n")
    fun bind(gameDealPOJO: GameDealPOJO) {
        val store = gameDealPOJO.storePOJOs?.filter {
            it.storeID == gameDealPOJO.storeID
        }?.get(0)

        itemView.findViewById<ImageView>(R.id.imageViewStoreLogo).loadImage(
            "https://www.cheapshark.com" +
                    store?.images?.get("logo")
        )
        val storeName = itemView.findViewById<TextView>(R.id.textViewStoreName)
        storeName.text = store?.storeName
        storeName.isSelected = true  //Enable marquee

        val oldPrice = itemView.findViewById<TextView>(R.id.textViewOldPrice)
        oldPrice.text = "$${gameDealPOJO.normalPrice}"
        itemView.findViewById<TextView>(R.id.textViewCurrentPrice).text = "$${gameDealPOJO.salePrice}"

        //Make old price strikethrough
        oldPrice.paintFlags = oldPrice
            .paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    companion object {
        fun from(itemView: View): DealsListViewHolder {
            return DealsListViewHolder(itemView)
        }
    }

}