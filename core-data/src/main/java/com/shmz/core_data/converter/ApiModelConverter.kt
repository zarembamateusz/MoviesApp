package com.shmz.core_data.converter


import com.shmz.core_api.model.MovieListItemResponse
import com.shmz.core_api.model.NowPlayingResponse
import com.shmz.core_data.constant.Constants.IMAGE_BASE_URL
import com.shmz.core_data.model.Movie
import com.shmz.core_data.model.PlayingInfo


fun NowPlayingResponse.asDomainModel(): PlayingInfo =
    PlayingInfo(
        page = page,
        movies = results.map { it.asDomainModel() },
        totalPages = total_pages,
        totalResults = total_results
    )

fun MovieListItemResponse.asDomainModel(): Movie =
    Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
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
        originalTitle = original_title
    )

fun String.asURL(): String = IMAGE_BASE_URL + this
