package com.mirkamal.gamewatch.network.services

import com.mirkamal.gamewatch.model.pojo.GameDealPOJO
import com.mirkamal.gamewatch.model.pojo.StorePOJO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mirkamal on 02 November 2020
 */
interface GameDealsService {

    @GET("deals")
    suspend fun getGameDeals(@Query("title") title: String): Response<List<GameDealPOJO>>

    @GET("stores")
    suspend fun getStores(): Response<List<StorePOJO>>
}