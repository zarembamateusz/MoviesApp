package com.shmz.core_domain

import com.shmz.core_data.repositories.NowPlayingRepository
import javax.inject.Inject

class FetchNowPlayingMoviesUseCase @Inject constructor(
    private val nowPlayingRepository: NowPlayingRepository,
) {

    suspend operator fun invoke(pageNumber: Int) =
        nowPlayingRepository.fetchMovies(pageNumber = pageNumber)
}