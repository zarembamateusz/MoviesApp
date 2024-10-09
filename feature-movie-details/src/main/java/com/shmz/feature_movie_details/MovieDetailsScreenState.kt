package com.shmz.feature_movie_details

import com.shmz.core_data.model.Movie

sealed interface MovieDetailsScreenState {
    data object Loading : MovieDetailsScreenState
    data object Error : MovieDetailsScreenState
    data class Idle(
        val movie: Movie
    ) : MovieDetailsScreenState
}