package com.mirkamal.gamewatch.network.services

import com.mirkamal.gamewatch.model.pojo.GamePOJO
import com.mirkamal.gamewatch.utils.IGDB_AUTHORIZATION
import com.mirkamal.gamewatch.utils.IGDB_CLIENT_ID
import com.mirkamal.gamewatch.utils.libs.network.ResponseParentData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Mirkamal on 18 October 2020
 */
interface SearchGameService {

    @POST("games/")
    suspend fun searchGames(
        @Header("Client-ID") clientID: String = IGDB_CLIENT_ID,
        @Header("Authorization") authorization: String = IGDB_AUTHORIZATION,
        @Body body: String
    ) : Response<ResponseParentData<List<GamePOJO>>>


}