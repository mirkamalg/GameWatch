package com.mirkamal.gamewatch.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Mirkamal on 30 October 2020
 */
@Entity(tableName = "games_table")
data class GameEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: Long,
    var rating: Int,
    var artworks: String,
    var cover: Long,
    var first_release_date: Long,
    var genres: String,
    var involved_companies: String,
    var name: String,
    var platforms: String,
    var screenshots: String,
    var similar_games: String,
    var summary: String,
    var url: String,
    var videos: String,
    var coverURL: String
)