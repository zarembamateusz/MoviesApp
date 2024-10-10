package com.shmz.core_data.repositories

import com.shmz.core_api.MovieApi
import com.shmz.core_api.MovieApiResult
import com.shmz.core_data.converter.asDomainModel
import com.shmz.core_database.dao.MovieInfoDao
import com.shmz.core_database.entity.MovieInfo
import com.shmz.core_model.results.MovieResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface MovieRepository {
    suspend fun fetchMovie(movieId: Int): Flow<MovieResult>
    suspend fun changeFavoriteState(movieId: Int, isFavorite: Boolean)
}

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val movieInfoDao: MovieInfoDao,
) : MovieRepository {

    override suspend fun fetchMovie(movieId: Int): Flow<MovieResult> {
        val responseFlow = flowOf(
            api.fetchMovie(movieId)
        )
        val isFavoriteFlow = movieInfoDao.isFavorite(movieId)
        return combine(
            responseFlow,
            isFavoriteFlow
        ) { response, isFavorite ->
            when (response) {
                MovieApiResult.NetworkError -> MovieResult.NetworkError
                is MovieApiResult.Success -> MovieResult.Success(
                    movie = response.movie.asDomainModel(
                        isFavorite ?: false
                    )
                )

                MovieApiResult.UnexpectedError -> MovieResult.UnexpectedError
            }
        }
    }

    override suspend fun changeFavoriteState(movieId: Int, isFavorite: Boolean) {
        movieInfoDao.insertMovieInfo(MovieInfo(id = movieId, isFavorite = isFavorite))
    }
}