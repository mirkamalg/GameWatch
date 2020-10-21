package com.mirkamal.gamewatch.repositories

import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.model.pojo.CoverPOJO
import com.mirkamal.gamewatch.network.ApiInitHelper
import com.mirkamal.gamewatch.utils.libs.coverPOJOListToCoverPOJOMap
import com.mirkamal.gamewatch.utils.toGameEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Created by Mirkamal on 18 October 2020
 */
class DiscoverGamesRepository : ParentRepository() {

    private val searchGameService = ApiInitHelper.searchGameService

    // Get pojos but return Game entities so that image URL can be sent to itemview of recyclerview
    suspend fun searchForGames(name: String): ArrayList<Game>? {
        return try {

            val body =
                "search \"$name\"; fields *; limit 25;".toRequestBody("text/plain".toMediaTypeOrNull())
            val mainResponse = searchGameService.searchGames(body = body)

            if (mainResponse.isSuccessful) {
                val responseBody = mainResponse.body()!!

                val ids = arrayListOf<Long>()
                for (item in responseBody) {
                    ids.add(item.cover ?: 0)
                }

                val coverPojos = fetchCoverURLs(ids)
                val coverPojoMap = coverPOJOListToCoverPOJOMap(coverPojos)
                val games = arrayListOf<Game>()

                for (game in responseBody) {
                    val temp = game.toGameEntity()
                    if (game.cover != null) {
                        temp.coverURL = coverPojoMap[game.cover]?.url ?: "null"
                    } else {
                        temp.coverURL = "null"
                    }
                    games.add(temp)
                }
                games
            } else {
                null
            }
        } catch (e: IOException) {
            arrayListOf()
        }
    }

    private suspend fun fetchCoverURLs(IDs: ArrayList<Long>): List<CoverPOJO> {
        return try {
            val body =
                "fields url; where id = (${IDs.joinToString(", ")}); limit 25;".toRequestBody("text/plain".toMediaTypeOrNull())
            val response = searchGameService.fetchCovers(body = body)
            val responseBody = response.body()

            if (responseBody != null) {

                val parsedCovers = arrayListOf<CoverPOJO>()

                for (item in responseBody) {
                    item.url = "https:" + item.url
                    parsedCovers.add(item)
                }
                parsedCovers
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            emptyList()
        }
    }

}