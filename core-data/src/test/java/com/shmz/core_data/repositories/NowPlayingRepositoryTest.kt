package com.shmz.core_data.repositories

import com.shmz.core_api.NowPlayingApi
import com.shmz.core_api.NowPlayingApiResult
import com.shmz.core_database.dao.MovieInfoDao
import com.shmz.core_model.results.NowPlayingResult
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
class NowPlayingRepositoryTest {

    private val api: NowPlayingApi = mockk<NowPlayingApi>()
    private val movieInfoDao: MovieInfoDao = mockk<MovieInfoDao>()
    private lateinit var repository: NowPlayingRepositoryImpl


    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = NowPlayingRepositoryImpl(api, movieInfoDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMovies returns success result when API call succeeds`() = runTest {
        // Given
        val movieId = 1
        val pageNumber = 1
        val apiResponse = NowPlayingApiResult.Success(
            createMockNowPlayingResponse(
                results = listOf(createMockMovieListItemResponse(id = movieId))
            )
        )

        coEvery { api.fetchMovies(pageNumber) } returns apiResponse
        every { movieInfoDao.getFavorites(listOf(movieId)) } returns flowOf(listOf(movieId))

        // When
        val result = repository.fetchMovies(pageNumber).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is NowPlayingResult.Success)
        val successResult = result as NowPlayingResult.Success
        assertEquals(1, successResult.result.movies.size)
        assertEquals(movieId, successResult.result.movies.first().id)
    }

    @Test
    fun `fetchMovies returns NetworkError when API returns NetworkError`() = runTest {
        // Given
        val pageNumber = 1
        coEvery { api.fetchMovies(pageNumber) } returns NowPlayingApiResult.NetworkError

        // When
        val result = repository.fetchMovies(pageNumber).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is NowPlayingResult.NetworkError)
    }

    @Test
    fun `fetchMovies returns UnexpectedError when API returns UnexpectedError`() = runTest {
        // Given
        val pageNumber = 1
        coEvery { api.fetchMovies(pageNumber) } returns NowPlayingApiResult.UnexpectedError

        // When
        val result = repository.fetchMovies(pageNumber).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is NowPlayingResult.UnexpectedError)
    }

    @Test
    fun `fetchMovies returns success result with correct favorite state`() = runTest {
        // Given
        val movieId = 1
        val pageNumber = 1
        val apiResponse = NowPlayingApiResult.Success(
            createMockNowPlayingResponse(
                results = listOf(createMockMovieListItemResponse(id = movieId))
            )
        )

        coEvery { api.fetchMovies(pageNumber) } returns apiResponse
        every { movieInfoDao.getFavorites(listOf(movieId)) } returns flowOf(emptyList())

        // When
        val result = repository.fetchMovies(pageNumber).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is NowPlayingResult.Success)
        val successResult = result as NowPlayingResult.Success
        assertEquals(1, successResult.result.movies.size)
        assertEquals(movieId, successResult.result.movies.first().id)
        assertFalse(successResult.result.movies.first().isFavorite)
    }

    @Test
    fun `fetchMovies returns success result with favorite state`() = runTest {
        // Given
        val movieId = 1
        val pageNumber = 1
        val apiResponse = NowPlayingApiResult.Success(
            createMockNowPlayingResponse(
                results = listOf(createMockMovieListItemResponse(id = movieId))
            )
        )

        coEvery { api.fetchMovies(pageNumber) } returns apiResponse
        every { movieInfoDao.getFavorites(listOf(movieId)) } returns flowOf(listOf(movieId))

        // When
        val result = repository.fetchMovies(pageNumber).first()

        advanceUntilIdle()

        // Then
        assertTrue(result is NowPlayingResult.Success)
        val successResult = result as NowPlayingResult.Success
        assertTrue(successResult.result.movies.first().isFavorite)
    }
}