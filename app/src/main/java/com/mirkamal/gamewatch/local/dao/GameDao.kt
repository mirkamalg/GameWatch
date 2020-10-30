package com.mirkamal.gamewatch.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mirkamal.gamewatch.model.entity.GameEntity

/**
 * Created by Mirkamal on 30 October 2020
 */

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGame(gameEntity: GameEntity)

    @Query("select * from games_table where id = :ID")
    fun getGameByID(ID: Long): GameEntity

}