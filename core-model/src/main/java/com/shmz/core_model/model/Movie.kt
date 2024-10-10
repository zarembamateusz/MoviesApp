package com.shmz.core_model.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val isAdult: Boolean,
    val isVideo: Boolean,
    val popularity: Double,
    val genres: List<Int>?,
    val originalLanguage: String,
    val originalTitle: String,
    val isFavorite: Boolean = false
)
