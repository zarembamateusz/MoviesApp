package com.shmz.feature_movie_details

sealed interface MovieDetailsScreenState {
    data object Loading : MovieDetailsScreenState
    data object Error : MovieDetailsScreenState
    data class Idle(
        val movie: com.shmz.core_model.model.Movie
    ) : MovieDetailsScreenState
}