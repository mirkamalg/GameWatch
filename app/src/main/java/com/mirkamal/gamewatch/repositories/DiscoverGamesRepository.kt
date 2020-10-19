package com.mirkamal.gamewatch.repositories

import com.mirkamal.gamewatch.network.ApiInitHelper
import com.mirkamal.gamewatch.utils.libs.network.NetworkState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesRepository : ParentRepository() {

    private val searchGameService = ApiInitHelper.searchGameService

    suspend fun searchForGames(name: String): NetworkState {
        return try {
            val body =
                "fields *; where name = \"$name\";".toRequestBody("text/plain".toMediaTypeOrNull())
            val response = searchGameService.searchGames(body = body)

            getNetworkState(response)
        } catch (e: IOException) {
            NetworkState.NetworkException(e.message!!)
        }
    }

    suspend fun fetchCoverURLs(IDs: Array<Long>): NetworkState {
        return try {
            val body = "(${IDs.joinToString(", ")})".toRequestBody("text/plain".toMediaTypeOrNull())
            val response = searchGameService.fetchCoverURLs(body = body)
            getNetworkState(response)
        } catch (e: IOException) {
            NetworkState.NetworkException(e.message!!)
        }
    }

}