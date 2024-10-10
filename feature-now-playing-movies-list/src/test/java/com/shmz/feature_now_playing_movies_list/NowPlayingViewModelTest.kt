package com.shmz.feature_now_playing_movies_list

import com.shmz.core_data.repositories.NowPlayingResult
import com.shmz.core_domain.ChangeFavoriteStateUseCase
import com.shmz.core_domain.FetchNowPlayingMoviesUseCase
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class NowPlayingViewModelTest {
    private val fetchNowPlayingMoviesUseCase: FetchNowPlayingMoviesUseCase = mockk()
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase = mockk(relaxed = true)
    private val ioDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private lateinit var viewModel: NowPlayingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(ioDispatcher)
        viewModel = NowPlayingViewModel(
            fetchNowPlayingMoviesUseCase = fetchNowPlayingMoviesUseCase,
            changeFavoriteStateUseCase = changeFavoriteStateUseCase,
            ioDispatcher = ioDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load movies on startup`() = runTest {
        // Given
        val playingInfo = createMockPlayingInfo()
        coEvery { fetchNowPlayingMoviesUseCase(1) } returns flowOf(
            NowPlayingResult.Success(
                playingInfo
            )
        )

        advanceUntilIdle()

        // Then
        assertEquals(NowPlayingListState.Idle(playingInfo), viewModel.screenState.value)
    }

    @Test
    fun `loadMovies should emit Error when network error occurs`() = runTest {
        // Given
        coEvery { fetchNowPlayingMoviesUseCase(1) } returns flowOf(NowPlayingResult.NetworkError)

        // When
        viewModel.onPreviousPage(1)

        advanceUntilIdle()

        // Then
        assertTrue(viewModel.screenState.first() is NowPlayingListState.Error)
    }

    @Test
    fun `loadMovies should emit Error when unexpected error occurs`() = runTest {
        // Given
        coEvery { fetchNowPlayingMoviesUseCase(1) } returns flowOf(NowPlayingResult.UnexpectedError)

        // When
        viewModel.onPreviousPage(1)

        advanceUntilIdle()

        // Then
        assertTrue(viewModel.screenState.first() is NowPlayingListState.Error)
    }
}