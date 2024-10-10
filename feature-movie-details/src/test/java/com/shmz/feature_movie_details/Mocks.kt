package com.shmz.feature_movie_details

import com.shmz.core_model.model.Movie
import io.mockk.every
import io.mockk.mockk

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