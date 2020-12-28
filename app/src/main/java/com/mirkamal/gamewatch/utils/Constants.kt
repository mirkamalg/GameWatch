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
const val IGDB_AUTHORIZATION = "Bearer jb3480t2xxfvhh9gkgqzzvueulh70p"
const val IGDB_BASE_URL = "https://api.igdb.com/v4/"

//Network constants CheapShark
const val CHEAPSHARK_BASE_URL = "https://www.cheapshark.com/api/1.0/"

//Platform IDs
const val PC_ID = 6
val XBOX_IDs = listOf(12, 49, 169) //XBox 360, one, series x
val PLAYSTATION_IDs = listOf(9, 48, 167) //PS3, PS4, PS5
const val NINTENDO_SWITCH_ID = 130

//Firestore keys
const val USER_DATA_COLLECTION_KEY = "userdata"
const val GAMES_KEY = "games"
const val EMAIL_KEY = "email"
const val USERNAME_KEY = "username"
const val BIO_KEY = "bio"
const val STEAM_KEY = "steam"
const val EPIC_GAMES_KEY = "epic_games"
const val UPLAY_KEY = "uplay"
const val DISCORD_KEY = "discord"
const val DISPLAY_NAME_KEY = "display_name"
const val URL_KEY = "url"

//Local database
const val LOCAL_DATABASE_NAME = "gamesdb"

//Intent keys
const val EXTRA_GAME_KEY = "EXTRA_GAME_KEY"
const val EXTRA_PROFILE_DATA_KEY = "EXTRA_PROFILE_DATA_KEY"

//Account types
const val ACCOUNT_STEAM = "ACCOUNT_STEAM"
const val ACCOUNT_EPIC_GAMES = "ACCOUNT_EPIC_GAMES"
const val ACCOUNT_UPLAY = "ACCOUNT_UPLAY"
const val ACCOUNT_DISCORD = "ACCOUNT_DISCORD"

//Regex
const val REGEX_STEAM_PROFILE_URL = "(?:https?:\\/\\/)?steamcommunity\\.com\\/(?:profiles|id)\\/[a-zA-Z0-9]+"

//Status
const val SUCCESS = "SUCCESS"
const val FAILURE = "FAILURE"