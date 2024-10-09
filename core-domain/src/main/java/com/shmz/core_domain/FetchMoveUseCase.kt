package com.shmz.core_domain

import com.shmz.core_data.repositories.MovieRepository
import javax.inject.Inject

class FetchMoveUseCase @Inject constructor(
    private val moviesRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Int) = moviesRepository.fetchMovie(movieId)
}