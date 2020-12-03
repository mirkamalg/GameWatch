package com.mirkamal.gamewatch.repositories

import android.util.Log
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.model.pojo.CoverPOJO
import com.mirkamal.gamewatch.model.pojo.GameDealPOJO
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO
import com.mirkamal.gamewatch.network.ApiInitHelper
import com.mirkamal.gamewatch.utils.libs.coverPOJOListToCoverPOJOMap
import com.mirkamal.gamewatch.utils.toGameEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Created by Mirkamal on 18 October 2020
 */
class GamesRepository() : ParentRepository() {

    private val searchGameService = ApiInitHelper.searchGameService
    private val gameDetailsService = ApiInitHelper.gameDetailsService
    private val gameDealsService = ApiInitHelper.gameDealsService

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

            if (response.isSuccessful && responseBody != null) {

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

    suspend fun fetchCoverByGameID(gameID: Long): String {
        return try {
            val body =
                "fields url; where game = $gameID;".toRequestBody("text/plain".toMediaTypeOrNull())
            val response = searchGameService.fetchCovers(body = body)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                return responseBody[0].url ?: ""
            }
            ""  //Return empty string
        } catch (e: IOException) {
            ""
        }
    }

    suspend fun fetchGenreByIDs(genreIDs: String): String {
        return try {
            val body =
                "fields name; where id = ($genreIDs);".toRequestBody("text/plain".toMediaTypeOrNull())
            val response = gameDetailsService.fetchGameGenre(body = body)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                val genres = arrayListOf<String>()
                for (genrePOJO in responseBody) {
                    genres.add(genrePOJO.name ?: "")
                }
                return genres.joinToString(", ")
            }
            ""  //Return empty string
        } catch (e: IOException) {
            ""
        }
    }

    suspend fun fetchGamesByIDs(ids: List<Long>): ArrayList<Game>? {
        return try {
            val body =
                "fields *; where id = (${ids.joinToString(", ")});".toRequestBody("text/plain".toMediaTypeOrNull())
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

    suspend fun fetchScreenshots(gameID: Long): List<ScreenshotPOJO> {
        return try {
            val body =
                "fields *; where game = $gameID;".toRequestBody("text/plain".toMediaTypeOrNull())
            val response = gameDetailsService.fetchScreenshots(body = body)
            val responseBody = response.body()

            if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                Log.e("GameRepository", "Screenshots response is successful and not null")
                return responseBody
            }
            emptyList()
        } catch (e: IOException) {
            emptyList()
        }
    }


    suspend fun fetchGameDeals(title: String): List<GameDealPOJO> {
        return try {
            val responseDeals = gameDealsService.getGameDeals(title)
            val responseStores = gameDealsService.getStores()

            val responseDealsBody = responseDeals.body()?.filter { it.isOnSale == "1" }
            val responseStoresBody = responseStores.body()

            if (responseDeals.isSuccessful && responseStores.isSuccessful
                && responseDealsBody != null && responseStoresBody != null
            ) {
                //Optimize this approach
                responseDealsBody.forEach {
                    it.storePOJOs = responseStoresBody
                }
                return responseDealsBody
            }
            emptyList()
        } catch (e: IOException) {
            emptyList()
        }
    }

}