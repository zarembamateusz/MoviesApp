package com.shmz.core_data

import com.shmz.core_api.NowPlayingApi
import com.shmz.core_api.NowPlayingApiResult
import com.shmz.core_data.converter.asDomainModel
import javax.inject.Inject

interface NowPlayingRepository {
    suspend fun fetchMovies(pageNumber: Int): NowPlayingResult
}

class NowPlayingRepositoryImpl @Inject constructor(private val api: NowPlayingApi) :
    NowPlayingRepository {

    override suspend fun fetchMovies(pageNumber: Int): NowPlayingResult {
        return when (val response = api.fetchMovies(pageNumber)) {
            NowPlayingApiResult.NetworkError -> NowPlayingResult.NetworkError
            NowPlayingApiResult.UnexpectedError -> NowPlayingResult.UnexpectedError
            is NowPlayingApiResult.Success -> {
                NowPlayingResult.Success(result = response.result.asDomainModel())
            }
        }
    }
}

sealed interface NowPlayingResult {
    data class Success(
        val result: com.shmz.core_data.model.PlayingInfo
    ) : NowPlayingResult

    data object NetworkError : NowPlayingResult

    data object UnexpectedError : NowPlayingResult
}