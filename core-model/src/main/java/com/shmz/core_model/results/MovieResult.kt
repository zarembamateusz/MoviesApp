package com.shmz.core_model.results

sealed interface MovieResult {
    data class Success(
        val movie: com.shmz.core_model.model.Movie
    ) : MovieResult

    data object NetworkError : MovieResult

    data object UnexpectedError : MovieResult
}