package com.shmz.core_api

import com.shmz.core_api.model.MovieListItemResponse
import com.shmz.core_api.services.MovieApiService
import javax.inject.Inject

interface MovieApi {
    suspend fun fetchMovie(movieId: Int): MovieApiResult
}

class MovieApiImpl @Inject constructor(
    private val apiService: MovieApiService
) : MovieApi {

    override suspend fun fetchMovie(movieId: Int): MovieApiResult {
        val response = apiService.getMovieDetails(movieId = movieId)
        return if (response.isSuccessful) {
            response.body()?.let {
                MovieApiResult.Success(
                    movie = it
                )
            } ?: MovieApiResult.UnexpectedError
        } else {
            MovieApiResult.NetworkError
        }
    }
}

sealed interface MovieApiResult {
    data class Success(
        val movie: MovieListItemResponse
    ) : MovieApiResult

    data object NetworkError : MovieApiResult

    data object UnexpectedError : MovieApiResult
}