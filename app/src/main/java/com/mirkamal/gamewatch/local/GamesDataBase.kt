package com.mirkamal.gamewatch.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mirkamal.gamewatch.local.dao.GameDao
import com.mirkamal.gamewatch.model.entity.GameEntity
import com.mirkamal.gamewatch.utils.LOCAL_DATABASE_NAME

/**
 * Created by Mirkamal on 30 October 2020
 */

@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class GamesDataBase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GamesDataBase? = null

        fun getInstance(context: Context): GamesDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamesDataBase::class.java,
                    LOCAL_DATABASE_NAME
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}