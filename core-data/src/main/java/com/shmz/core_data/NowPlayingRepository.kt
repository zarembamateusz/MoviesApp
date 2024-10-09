package com.shmz.core_data

import com.shmz.core_api.NowPlayingApi
import com.shmz.core_api.NowPlayingApiResult
import com.shmz.core_data.converter.asDomainModel
import com.shmz.core_data.model.PlayingInfo
import com.shmz.core_database.dao.MovieInfoDao
import com.shmz.core_database.entity.MovieInfo
import javax.inject.Inject

interface NowPlayingRepository {
    suspend fun fetchMovies(pageNumber: Int): NowPlayingResult
    suspend fun changeFavoriteState(movieId: Int, isFavorite: Boolean)
}

class NowPlayingRepositoryImpl @Inject constructor(
    private val api: NowPlayingApi,
    private val movieInfoDao: MovieInfoDao
) : NowPlayingRepository {

    override suspend fun fetchMovies(pageNumber: Int): NowPlayingResult {
        return when (val response = api.fetchMovies(pageNumber)) {
            NowPlayingApiResult.NetworkError -> NowPlayingResult.NetworkError
            NowPlayingApiResult.UnexpectedError -> NowPlayingResult.UnexpectedError
            is NowPlayingApiResult.Success -> {
                val playingInfo = PlayingInfo(
                    page = response.result.page,
                    movies = response.result.results.map {
                        it.asDomainModel(
                            movieInfoDao.isFavorite(
                                it.id
                            ) ?: false
                        )
                    },
                    totalPages = response.result.total_pages,
                    totalResults = response.result.total_results
                )
                NowPlayingResult.Success(result = playingInfo)
            }
        }
    }

    override suspend fun changeFavoriteState(movieId: Int, isFavorite: Boolean) {
        movieInfoDao.insertMovieInfo(MovieInfo(id = movieId, isFavorite = isFavorite))
    }
}

sealed interface NowPlayingResult {
    data class Success(
        val result: PlayingInfo
    ) : NowPlayingResult

    data object NetworkError : NowPlayingResult

    data object UnexpectedError : NowPlayingResult
}