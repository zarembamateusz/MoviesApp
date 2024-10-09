package com.shmz.core_domain

import com.shmz.core_data.repositories.MovieRepository
import javax.inject.Inject

class ChangeFavoriteStateUseCase @Inject constructor(
    private val moviesRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Int, isFavorite: Boolean) {
        moviesRepository.changeFavoriteState(movieId = movieId, isFavorite = isFavorite)
    }
}