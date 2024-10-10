package com.shmz.feature_now_playing_movies_list

import com.shmz.core_data.model.Movie
import com.shmz.core_data.model.PlayingInfo
import io.mockk.every
import io.mockk.mockk

fun createMockPlayingInfo(
    page: Int = 1,
    movies: List<Movie> = listOf(createMockMovie()),
    totalPages: Int = 10,
    totalResults: Int = 100
): PlayingInfo {
    return PlayingInfo(
        page = page,
        movies = movies,
        totalPages = totalPages,
        totalResults = totalResults
    )
}

fun createMockMovie(
    id: Int = 1,
    title: String = "Mock Title",
    overview: String = "Mock Overview",
    releaseDate: String = "2024-10-10",
    posterUrl: String? = "https://example.com/poster.jpg",
    backdropUrl: String? = "https://example.com/backdrop.jpg",
    voteAverage: Double = 8.5,
    voteCount: Int = 1000,
    isAdult: Boolean = false,
    isVideo: Boolean = false,
    popularity: Double = 100.0,
    genres: List<Int>? = listOf(28, 12),
    originalLanguage: String = "en",
    originalTitle: String = "Original Mock Title",
    isFavorite: Boolean = false
): Movie {
    val movieMock = mockk<Movie>()
    every { movieMock.id } returns id
    every { movieMock.title } returns title
    every { movieMock.overview } returns overview
    every { movieMock.releaseDate } returns releaseDate
    every { movieMock.posterUrl } returns posterUrl
    every { movieMock.backdropUrl } returns backdropUrl
    every { movieMock.voteAverage } returns voteAverage
    every { movieMock.voteCount } returns voteCount
    every { movieMock.isAdult } returns isAdult
    every { movieMock.isVideo } returns isVideo
    every { movieMock.popularity } returns popularity
    every { movieMock.genres } returns genres
    every { movieMock.originalLanguage } returns originalLanguage
    every { movieMock.originalTitle } returns originalTitle
    every { movieMock.isFavorite } returns isFavorite

    return movieMock
}