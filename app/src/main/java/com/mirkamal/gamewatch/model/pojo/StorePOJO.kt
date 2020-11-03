package com.mirkamal.gamewatch.model.pojo

/**
 * Created by Mirkamal on 02 November 2020
 */
data class StorePOJO(
    val storeID: String?,
    val storeName: String?,
    val isActive: Int?,
    val images: Map<String, String>?
)