package com.shmz.core_model.model

data class PlayingInfo(
    val page: Int,
    val movies: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)
