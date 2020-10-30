package com.mirkamal.gamewatch.utils

/**
 * Created by Mirkamal on 18 October 2020
 */

// My games inner fragment types
const val TYPE_PLAYING = 0
const val TYPE_WANT_TO_PLAY = 1
const val TYPE_PLAYED = 2

// Discover inner fragment types
const val TYPE_GAMES = 3
const val TYPE_USERS = 4

//Network constants IGDB
const val IGDB_CLIENT_ID = "4iltm3ili411wdllzr2l95m4dh9315"
const val IGDB_AUTHORIZATION = "Bearer ibfadvnqgnjv2mwr9z41u6b4wz8p45"
const val IGDB_BASE_URL = "https://api.igdb.com/v4/"

//Platform IDs
const val PC_ID = 6
val XBOX_IDs = listOf(12, 49, 169) //XBox 360, one, series x
val PLAYSTATION_IDs = listOf(9, 48, 167) //PS3, PS4, PS5
const val NINTENDO_SWITCH_ID = 130

const val USER_DATA_COLLECTION_KEY = "userdata"
const val GAMES_KEY = "games"
const val EMAIL_KEY = "email"

//Local database
const val LOCAL_DATABASE_NAME = "gamesdb"