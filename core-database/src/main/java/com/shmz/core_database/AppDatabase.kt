package com.shmz.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shmz.core_database.dao.MovieInfoDao
import com.shmz.core_database.entity.MovieInfo

@Database(
    entities =
    [
        MovieInfo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieInfoDao(): MovieInfoDao
}