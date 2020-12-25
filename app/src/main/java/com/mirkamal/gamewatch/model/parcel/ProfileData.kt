package com.mirkamal.gamewatch.model.parcel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Mirkamal on 25 December 2020
 */

@Parcelize
data class ProfileData(
    val username: String,
    val bio: String,
    val profilePictureURL: String,
    val steam: Map<String, String>,
    val epicGames: Map<String, String>,
    val uplay: Map<String, String>,
    val discord: Map<String, String>
): Parcelable