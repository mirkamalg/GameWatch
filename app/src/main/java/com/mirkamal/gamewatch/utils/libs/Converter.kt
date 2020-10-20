package com.mirkamal.gamewatch.utils.libs

import com.mirkamal.gamewatch.model.pojo.CoverPOJO

/**
 * Created by Mirkamal on 20 October 2020
 */

fun coverPOJOListToCoverPOJOMap(coverPojoList: List<CoverPOJO>): Map<Long, CoverPOJO> {
    val map = mutableMapOf<Long, CoverPOJO>()
    for (coverPojo in coverPojoList) {
        coverPojo.id?.let { map.put(it, coverPojo) }
    }
    return map
}