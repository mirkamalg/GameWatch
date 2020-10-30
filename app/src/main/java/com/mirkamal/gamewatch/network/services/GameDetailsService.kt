package com.mirkamal.gamewatch.network.services

import com.mirkamal.gamewatch.model.pojo.GenrePOJO
import com.mirkamal.gamewatch.model.pojo.ScreenshotPOJO
import com.mirkamal.gamewatch.utils.IGDB_AUTHORIZATION
import com.mirkamal.gamewatch.utils.IGDB_CLIENT_ID
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by Mirkamal on 30 October 2020
 */
interface GameDetailsService {

    @POST("screenshots/")
    suspend fun fetchScreenshots(
        @Header("Client-ID") clientID: String = IGDB_CLIENT_ID,
        @Header("Authorization") authorization: String = IGDB_AUTHORIZATION,
        @Body body: RequestBody
    ): Response<List<ScreenshotPOJO>>

    @POST("genres/")
    suspend fun fetchGameGenre(
        @Header("Client-ID") clientID: String = IGDB_CLIENT_ID,
        @Header("Authorization") authorization: String = IGDB_AUTHORIZATION,
        @Body body: RequestBody
    ) : Response<List<GenrePOJO>>
}