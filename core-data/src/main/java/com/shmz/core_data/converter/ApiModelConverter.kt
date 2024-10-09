package com.shmz.core_data.converter


import com.shmz.core_api.model.MovieListItemResponse
import com.shmz.core_data.constant.Constants.IMAGE_BASE_URL
import com.shmz.core_data.model.Movie

fun MovieListItemResponse.asDomainModel(isFavorite: Boolean): Movie =
    Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = release_date,
        posterUrl = poster_path?.asURL(),
        backdropUrl = backdrop_path?.asURL(),
        voteAverage = vote_average,
        voteCount = vote_count,
        isAdult = adult,
        isVideo = video,
        popularity = popularity,
        genres = genre_ids,
        originalLanguage = original_language,
        originalTitle = original_title,
        isFavorite = isFavorite
    )

fun String.asURL(): String = IMAGE_BASE_URL + this
