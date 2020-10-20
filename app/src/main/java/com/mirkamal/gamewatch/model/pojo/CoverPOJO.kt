package com.mirkamal.gamewatch.model.pojo

import com.squareup.moshi.Json

/**
 * Created by Mirkamal on 19 October 2020
 */
data class CoverPOJO(
    @Json(name = "id") val id: Long?,
    @Json(name = "url") var url: String?
)