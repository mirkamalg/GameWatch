package com.mirkamal.gamewatch.model.pojo

/**
 * Created by Mirkamal on 30 October 2020
 */
data class ScreenshotPOJO(
    private val id: Long?,
    private val game: Long?,
    private val height: Int?,
    private val image_id: String?,
    val url: String?,
    private val width: Int?,
    private val checksum: String?
)