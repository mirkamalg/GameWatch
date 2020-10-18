package com.mirkamal.gamewatch.model.pojo

/**
 * Created by Mirkamal on 18 October 2020
 */
data class GamePOJO(
    private val id: Int?,
    private val aggregated_rating: Double?,
    private val artworks: List<Int>?,
    private val cover: Int?,
    private val first_release_date: Long?,
    private val genres: List<Int>?,
    private val involved_companies: List<Int>?,
    private val name: String?,
    private val platforms: List<Int>?,
    private val screenshots: List<Int>?,
    private val similar_games: List<Int>?,
    private val summary: String?,
    private val url: String?,
    private val videos: List<Int>?
)