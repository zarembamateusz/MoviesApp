package com.shmz.core_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shmz.core_database.entity.MovieInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieInfo(movieInfo: MovieInfo)


    @Query("SELECT isFavorite FROM movie_info WHERE id = :movieId")
    fun isFavorite(movieId: Int): Flow<Boolean?>

    @Query("SELECT id FROM movie_info WHERE id in (:movieIds) and isFavorite = 1")
    fun getFavorites(movieIds: List<Int>): Flow<List<Int>>

    @Query("DELETE FROM movie_info")
    suspend fun deleteAllEventLogs()
}
