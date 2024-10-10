package com.shmz.feature_movie_details

import com.shmz.core_domain.ChangeFavoriteStateUseCase
import com.shmz.core_domain.FetchMoveUseCase
import com.shmz.core_model.results.MovieResult
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class MovieDetailsViewModelTest {

    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase = mockk(relaxed = true)
    private val fetchMoveUseCase: FetchMoveUseCase = mockk()
    private val ioDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(ioDispatcher)
        viewModel = MovieDetailsViewModel(
            changeFavoriteStateUseCase = changeFavoriteStateUseCase,
            fetchMoveUseCase = fetchMoveUseCase,
            ioDispatcher = ioDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onStart should emit Loading then Idle when movie fetch is successful`() = runTest {
        // Given
        val movieId = 1
        val mockMovie = createMockMovie(id = movieId)
        coEvery { fetchMoveUseCase(movieId) } returns flowOf(MovieResult.Success(mockMovie))

        // When
        viewModel.onStart(movieId)

        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailsScreenState.Idle(movie = mockMovie), viewModel.screenState.value)
    }

    @Test
    fun `onStart should emit Error when movie fetch results in NetworkError`() = runTest {
        // Given
        val movieId = 1
        coEvery { fetchMoveUseCase(movieId) } returns flowOf(MovieResult.NetworkError)

        // When
        viewModel.onStart(movieId)

        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailsScreenState.Error, viewModel.screenState.value)
    }

    @Test
    fun `onFavoriteToggle should call changeFavoriteStateUseCase with correct params`() = runTest {
        // Given
        val movieId = 1
        val isFavorite = true

        // When
        viewModel.onFavoriteToggle(movieId, isFavorite)

        advanceUntilIdle()

        // Then
        coVerify { changeFavoriteStateUseCase(movieId, isFavorite) }
    }

    @Test
    fun `onStart should emit Error when movie fetch results in UnexpectedError`() = runTest {
        // Given
        val movieId = 1
        coEvery { fetchMoveUseCase(movieId) } returns flowOf(MovieResult.UnexpectedError)

        // When
        viewModel.onStart(movieId)

        advanceUntilIdle()

        // Then
        assertEquals(MovieDetailsScreenState.Error, viewModel.screenState.value)
    }
}