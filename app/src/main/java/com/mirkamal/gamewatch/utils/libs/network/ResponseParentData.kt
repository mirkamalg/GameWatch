package com.mirkamal.gamewatch.utils.libs.network

/**
 * Created by Niyazi on 8/10/2020
 */
data class ResponseParentData<T>(
    val status: Int? = 0,
    val data: T? = null
)