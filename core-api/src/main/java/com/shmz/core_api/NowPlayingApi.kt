package com.shmz.core_api

import com.shmz.core_api.model.NowPlayingResponse
import com.shmz.core_api.services.NowPlayingApiService
import javax.inject.Inject

interface NowPlayingApi {
    suspend fun fetchMovies(page: Int): NowPlayingApiResult
}

class NowPlayingApiImpl @Inject constructor(
    private val apiService: NowPlayingApiService
) : NowPlayingApi {

    override suspend fun fetchMovies(page: Int): NowPlayingApiResult {
        val response = apiService.getNowPlayingMovies(page)
        return if (response.isSuccessful) {
            response.body()?.let {
                NowPlayingApiResult.Success(
                    result = it
                )
            } ?: NowPlayingApiResult.UnexpectedError
        } else {
            NowPlayingApiResult.NetworkError
        }
    }
}

sealed interface NowPlayingApiResult {
    data class Success(
        val result: NowPlayingResponse
    ) : NowPlayingApiResult

    data object NetworkError : NowPlayingApiResult

    data object UnexpectedError : NowPlayingApiResult
}