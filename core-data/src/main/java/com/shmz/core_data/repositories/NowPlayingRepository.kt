package com.shmz.core_data.repositories

import com.shmz.core_api.NowPlayingApi
import com.shmz.core_api.NowPlayingApiResult
import com.shmz.core_data.converter.asDomainModel
import com.shmz.core_data.model.PlayingInfo
import com.shmz.core_database.dao.MovieInfoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NowPlayingRepository {
    suspend fun fetchMovies(pageNumber: Int): Flow<NowPlayingResult>
}

class NowPlayingRepositoryImpl @Inject constructor(
    private val api: NowPlayingApi,
    private val movieInfoDao: MovieInfoDao
) : NowPlayingRepository {

    override suspend fun fetchMovies(pageNumber: Int): Flow<NowPlayingResult> = flow {
        when (val response = api.fetchMovies(pageNumber)) {
            NowPlayingApiResult.NetworkError -> emit(NowPlayingResult.NetworkError)
            NowPlayingApiResult.UnexpectedError -> emit(NowPlayingResult.UnexpectedError)
            is NowPlayingApiResult.Success -> {
                val result = response.result
                movieInfoDao.getFavorites(result.results.map { it.id }).collect { favoriteMovies ->
                    val playingInfo = PlayingInfo(
                        page = result.page,
                        movies = result.results.map { movie ->
                            movie.asDomainModel(favoriteMovies.contains(movie.id))
                        },
                        totalPages = response.result.total_pages,
                        totalResults = response.result.total_results
                    )
                    emit(NowPlayingResult.Success(result = playingInfo))
                }
            }
        }
    }
}

sealed interface NowPlayingResult {
    data class Success(
        val result: PlayingInfo
    ) : NowPlayingResult

    data object NetworkError : NowPlayingResult

    data object UnexpectedError : NowPlayingResult
}