package com.shmz.core_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shmz.core_database.entity.MovieInfo

@Dao
interface MovieInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieInfo(movieInfo: MovieInfo)

    @Query("SELECT isFavorite FROM movie_info WHERE id = :movieId")
    suspend fun isFavorite(movieId: Int): Boolean?

    @Query("DELETE FROM movie_info")
    suspend fun deleteAllEventLogs()
}
