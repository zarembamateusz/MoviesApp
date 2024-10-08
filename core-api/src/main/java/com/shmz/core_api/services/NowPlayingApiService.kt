package com.shmz.core_api.services

import com.shmz.core_api.model.NowPlayingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NowPlayingApiService {

    @GET("/3/movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): Response<NowPlayingResponse>
}