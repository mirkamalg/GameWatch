package com.mirkamal.gamewatch.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mirkamal.gamewatch.BuildConfig
import com.mirkamal.gamewatch.network.services.GameDetailsService
import com.mirkamal.gamewatch.network.services.SearchGameService
import com.mirkamal.gamewatch.utils.IGDB_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Mirkamal on 18 October 2020
 */

//Convert json to kotlin object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


object ApiInitHelper {

    private val okHttpClientBuilder = OkHttpClient.Builder()
    private var retrofit: Retrofit? = null

    private fun okHttpClient(): OkHttpClient {
        when {
            BuildConfig.DEBUG -> {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder.addInterceptor(logging)
            }
        }
        return okHttpClientBuilder.build()
    }

    /**
     * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
     * object.
     */

    private fun getClient(): Retrofit {
        when (retrofit) {
            null -> {
                retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(okHttpClient())
                    .baseUrl(IGDB_BASE_URL)
                    .build()
            }
        }
        return retrofit as Retrofit
    }

    val searchGameService: SearchGameService by lazy { getClient().create(SearchGameService::class.java) }
    val gameDetailsService: GameDetailsService by lazy { getClient().create(GameDetailsService::class.java) }
}