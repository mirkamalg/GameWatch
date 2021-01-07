package com.mirkamal.gamewatch.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mirkamal.gamewatch.model.parcel.Game
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

@BindingAdapter("imageURL")
fun ImageView.loadImage(url: String) {
    Glide
        .with(this.context)
        .load(url)
//        .placeholder(R.drawable.drawable_loading_placeholder)
        .centerCrop()
        .into(this)
}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}

fun Fragment.openURL(URL: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
    startActivity(intent)
}

fun Fragment.copyToClipboard(text: String) {
    if (text.isNotEmpty()) {
        val clip = ClipData.newPlainText("gamewatch_label", text)
        (context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(clip)

        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
    }
}