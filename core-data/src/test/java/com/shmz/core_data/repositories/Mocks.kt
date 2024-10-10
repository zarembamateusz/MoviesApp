package com.shmz.core_data.repositories

import com.shmz.core_api.model.DatesResponse
import com.shmz.core_api.model.MovieListItemResponse
import com.shmz.core_api.model.NowPlayingResponse
import com.shmz.core_data.model.Movie
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

fun createMockMovieListItemResponse(
    adult: Boolean = false,
    backdropPath: String? = "https://example.com/backdrop.jpg",
    genreIds: List<Int>? = listOf(28, 12),
    id: Int = 1,
    originalLanguage: String = "en",
    originalTitle: String = "Mock Original Title",
    overview: String = "Mock Overview",
    popularity: Double = 100.0,
    posterPath: String? = "https://example.com/poster.jpg",
    releaseDate: String = "2024-10-10",
    title: String = "Mock Title",
    video: Boolean = false,
    voteAverage: Double = 8.5,
    voteCount: Int = 1000
): MovieListItemResponse {
    val mockResponse = mockk<MovieListItemResponse>()
    every { mockResponse.adult } returns adult
    every { mockResponse.backdrop_path } returns backdropPath
    every { mockResponse.genre_ids } returns genreIds
    every { mockResponse.id } returns id
    every { mockResponse.original_language } returns originalLanguage
    every { mockResponse.original_title } returns originalTitle
    every { mockResponse.overview } returns overview
    every { mockResponse.popularity } returns popularity
    every { mockResponse.poster_path } returns posterPath
    every { mockResponse.release_date } returns releaseDate
    every { mockResponse.title } returns title
    every { mockResponse.video } returns video
    every { mockResponse.vote_average } returns voteAverage
    every { mockResponse.vote_count } returns voteCount

    return mockResponse
}

fun createMockNowPlayingResponse(
    dates: DatesResponse = createMockDatesResponse(),
    page: Int = 1,
    results: List<MovieListItemResponse> = listOf(createMockMovieListItemResponse()),
    totalPages: Int = 1,
    totalResults: Int = results.size
): NowPlayingResponse {
    return NowPlayingResponse(
        dates = dates,
        page = page,
        results = results,
        total_pages = totalPages,
        total_results = totalResults
    )
}

fun createMockDatesResponse(
    maximum: String = "2024-12-31",
    minimum: String = "2024-01-01"
): DatesResponse {
    return DatesResponse(
        maximum = maximum,
        minimum = minimum
    )
}