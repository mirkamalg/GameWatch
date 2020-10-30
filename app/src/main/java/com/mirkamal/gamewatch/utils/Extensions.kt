package com.mirkamal.gamewatch.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mirkamal.gamewatch.R
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.model.entity.GameEntity
import com.mirkamal.gamewatch.model.pojo.GamePOJO

/**
 * Created by Mirkamal on 19 October 2020
 */

fun GamePOJO.toGameEntity(): Game {
    return Game(
        this.id ?: 0,
        this.rating?.toInt() ?: 0,
        this.artworks ?: emptyList(),
        this.cover ?: 0,
        this.first_release_date ?: 0,
        this.genres ?: emptyList(),
        this.involved_companies ?: emptyList(),
        this.name ?: "null",
        this.platforms ?: emptyList(),
        this.screenshots ?: emptyList(),
        this.similar_games ?: emptyList(),
        this.summary ?: "null",
        this.url ?: "null",
        this.videos ?: emptyList(),
        "null"
    )
}

fun Game.toGameEntity(): GameEntity {
    return GameEntity(
        this.id,
        this.rating,
        this.artworks.joinToString(", "),
        this.cover,
        this.first_release_date,
        this.genres.joinToString(", "),
        this.involved_companies.joinToString(", "),
        this.name,
        this.platforms.joinToString(", "),
        this.screenshots.joinToString(", "),
        this.similar_games.joinToString(", "),
        this.summary,
        this.url,
        this.videos.joinToString(", "),
        this.coverURL
    )
}

@BindingAdapter("imageURL")
fun ImageView.loadImage(url: String) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(R.drawable.drawable_loading_placeholder)
        .centerCrop()
        .into(this)
}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}