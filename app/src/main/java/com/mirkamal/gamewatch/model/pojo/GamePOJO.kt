package com.mirkamal.gamewatch.model.pojo

import com.squareup.moshi.Json

/**
 * Created by Mirkamal on 18 October 2020
 */
data class GamePOJO(
    @Json(name = "id") val id: Long?,
    @Json(name = "aggregated_rating") val aggregated_rating: Int?,
    @Json(name = "artworks") val artworks: List<Long>?,
    @Json(name = "cover") val cover: Long?,
    @Json(name = "first_release_date") val first_release_date: Long?,
    @Json(name = "genres") val genres: List<Int>?,
    @Json(name = "involved_companies") val involved_companies: List<Long>?,
    @Json(name = "name") val name: String?,
    @Json(name = "platforms") val platforms: List<Long>?,
    @Json(name = "screenshots") val screenshots: List<Long>?,
    @Json(name = "similar_games") val similar_games: List<Long>?,
    @Json(name = "summary") val summary: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "videos") val videos: List<Long>?
)