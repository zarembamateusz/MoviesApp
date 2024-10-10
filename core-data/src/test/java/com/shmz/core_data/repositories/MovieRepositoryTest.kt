package com.shmz.core_data.repositories

import com.shmz.core_api.MovieApi
import com.shmz.core_api.MovieApiResult
import com.shmz.core_database.dao.MovieInfoDao
import com.shmz.core_database.entity.MovieInfo
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class MovieRepositoryTest {

    private val api = mockk<MovieApi>()
    private val movieInfoDao = mockk<MovieInfoDao>()
    private lateinit var movieRepository: MovieRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        movieRepository = MovieRepositoryImpl(api, movieInfoDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMovie returns success when API returns success and movie is favorite`() = runTest {
        // Given
        val movieId = 123
        val mockApiResponse = MovieApiResult.Success(createMockMovieListItemResponse(id = movieId))
        val mockMovie = createMockMovie(id = movieId)

        coEvery { api.fetchMovie(movieId) } returns mockApiResponse
        every { movieInfoDao.isFavorite(movieId) } returns flowOf(true)

        // When
        val result = movieRepository.fetchMovie(movieId).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is MovieResult.Success)
        assertEquals(mockMovie.id, (result as MovieResult.Success).movie.id)
        assertEquals(true, result.movie.isFavorite)
    }

    @Test
    fun `fetchMovie returns NetworkError when API returns NetworkError`() = runTest {
        // Given
        val movieId = 123
        coEvery { api.fetchMovie(movieId) } returns MovieApiResult.NetworkError
        every { movieInfoDao.isFavorite(movieId) } returns flowOf(null)

        // When
        val result = movieRepository.fetchMovie(movieId).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is MovieResult.NetworkError)
    }

    @Test
    fun `fetchMovie returns UnexpectedError when API returns UnexpectedError`() = runTest {
        // Given
        val movieId = 123
        coEvery { api.fetchMovie(movieId) } returns MovieApiResult.UnexpectedError
        every { movieInfoDao.isFavorite(movieId) } returns flowOf(null)

        // When
        val result = movieRepository.fetchMovie(movieId).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is MovieResult.UnexpectedError)
    }

    @Test
    fun `fetchMovie returns success when API returns success and movie is not favorite`() =
        runTest {
            // Given
            val movieId = 123
            val mockApiResponse =
                MovieApiResult.Success(createMockMovieListItemResponse(id = movieId))
            val mockMovie = createMockMovie(id = movieId)

            coEvery { api.fetchMovie(movieId) } returns mockApiResponse
            every { movieInfoDao.isFavorite(movieId) } returns flowOf(false)

            // When
            val result = movieRepository.fetchMovie(movieId).first()

            advanceUntilIdle()

            // Then
            assertTrue(result is MovieResult.Success)
            assertEquals(mockMovie.id, (result as MovieResult.Success).movie.id)
            assertEquals(false, result.movie.isFavorite)
        }

    @Test
    fun `changeFavoriteState updates favorite state in database`() = runTest {
        // Given
        val movieId = 123
        val isFavorite = true
        coEvery { movieInfoDao.insertMovieInfo(any()) } just Runs

        // When
        movieRepository.changeFavoriteState(movieId, isFavorite)

        advanceUntilIdle()

        // Then
        coVerify {
            movieInfoDao.insertMovieInfo(
                MovieInfo(id = movieId, isFavorite = isFavorite)
            )
        }
    }
}
