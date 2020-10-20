package com.mirkamal.gamewatch.repositories

import android.util.Log
import com.mirkamal.gamewatch.model.entity.Game
import com.mirkamal.gamewatch.network.ApiInitHelper
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
                "fields *; where name = \"$name\";".toRequestBody("text/plain".toMediaTypeOrNull())
            val mainResponse = searchGameService.searchGames(body = body)

            if (mainResponse.isSuccessful) {
                val responseBody = mainResponse.body()!!

                val ids = arrayListOf<Long>()
                for (item in responseBody) {
                    ids.add(item.cover ?: 0)
                }

                val urls = fetchCoverURLs(ids)
                val games = arrayListOf<Game>()
                for (index in 0..games.size) {
                    try {
                        val temp = responseBody[index].toGameEntity()
                        temp.coverURL = urls[index]
                        games.add(temp)
                    } catch (e: Exception) {break}

//                val temp = item.toGameEntity()
//                temp.coverURL = urls[responseBody.indexOf(item)]
//                games.add(temp)
                }

                games
            } else {
                null
            }

        } catch (e: IOException) {
            arrayListOf()
        }
    }

    private suspend fun fetchCoverURLs(IDs: ArrayList<Long>): List<String> {
        return try {
            val body =
                "fields url; where id = (${IDs.joinToString(", ")});".toRequestBody("text/plain".toMediaTypeOrNull())
            val response = searchGameService.fetchCovers(body = body)
            val responseBody = response.body()

            if (responseBody != null) {
                val coverURLs = arrayListOf<String>()
                for (item in responseBody) {

                    //Refactor url to prevent Glide errors
                    item.url = "https://" + item.url?.removeRange(0, 2)
                    Log.e("HERE", item.url?:"null")
                    coverURLs.add(
                        item.url
                            ?: "https://us.123rf.com/450wm/pavelstasevich/pavelstasevich1811/pavelstasevich181101028/112815904-stock-vector-no-image-available-icon-flat-vector-illustration.jpg?ver=6"
                    )

                    if (item.url == null) {
                        Log.e("FETCH_COVER_URL", "URL is null")
                    } else {
                        Log.d("FETCH_COVER_URL", item.url!!)
                    }
                }
                coverURLs
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            emptyList()
        }
    }

}