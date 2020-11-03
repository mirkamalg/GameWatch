package com.mirkamal.gamewatch.model.pojo

/**
 * Created by Mirkamal on 02 November 2020
 */
data class GameDealPOJO(
    val internalName: String?,
    val title: String?,
    val metacriticLink: String?,
    val dealID: String?,
    val storeID: String?,
    val gameID: String?,
    val salePrice: String?,
    val normalPrice: String?,
    val isOnSale: String?,
    val savings: String?,
    val metacriticScore: String?,
    val steamRatingText: String?,
    val steamRatingPercent: String?,
    val steamRatingCount: String?,
    val steamAppID: String?,
    val releaseDate: Long?,
    val lastChange: Long?,
    val dealRating: String?,
    val thumb: String?,
    var storePOJOs: List<StorePOJO>?  //This is set manually later
)