package com.mirkamal.gamewatch.repositories

import com.mirkamal.gamewatch.network.ApiInitHelper
import com.mirkamal.gamewatch.utils.libs.network.NetworkState
import java.io.IOException

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesRepository : ParentRepository() {

    private val searchGameService = ApiInitHelper.searchGameService

    suspend fun searchForGames(name: String): NetworkState {
        return try {
            val response = searchGameService.searchGames(body = "fields *; where name = $name;")
            getNetworkState(response)
        } catch (e: IOException) {
            NetworkState.NetworkException(e.message!!)
        }
    }
}