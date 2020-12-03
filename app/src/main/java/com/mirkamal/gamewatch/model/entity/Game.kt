package com.mirkamal.gamewatch.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Mirkamal on 19 October 2020
 */
@Parcelize
data class Game(
    var id: Long,
    var rating: Int,
    var artworks: List<Long>,
    var cover: Long,
    var first_release_date: Long,
    var genres: List<Int>,
    var involved_companies: List<Long>,
    var name: String,
    var platforms: List<Long>,
    var screenshots: List<Long>,
    var similar_games: List<Long>,
    var summary: String,
    var url: String,
    var videos: List<Long>,
    var coverURL: String
): Parcelable