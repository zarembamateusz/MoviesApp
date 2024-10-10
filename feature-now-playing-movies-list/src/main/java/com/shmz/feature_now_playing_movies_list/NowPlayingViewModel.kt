package com.shmz.feature_now_playing_movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shmz.core_domain.ChangeFavoriteStateUseCase
import com.shmz.core_domain.FetchNowPlayingMoviesUseCase
import com.shmz.core_model.results.NowPlayingResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val fetchNowPlayingMoviesUseCase: FetchNowPlayingMoviesUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private var job: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _screenState.value = NowPlayingListState.Error(
            errorMessage = R.string.network_error,
            onRetry = { loadMovies(1) }
        )
    }
    private val _screenState: MutableStateFlow<NowPlayingListState> =
        MutableStateFlow(NowPlayingListState.Loading)
    val screenState: StateFlow<NowPlayingListState> = _screenState

    init {
        loadMovies(
            page = 1
        )
    }

    fun onFavoriteToggle(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            changeFavoriteStateUseCase(movieId, isFavorite)
        }
    }

    fun onPreviousPage(previousPage: Int) {
        _screenState.value = NowPlayingListState.Loading
        loadMovies(page = previousPage)
    }

    fun onNextPage(nextPage: Int) {
        _screenState.value = NowPlayingListState.Loading
        loadMovies(nextPage)
    }

    private fun loadMovies(page: Int) {
        viewModelScope.launch {
            job?.cancel()
            job = launch(ioDispatcher + coroutineExceptionHandler) {
                fetchNowPlayingMoviesUseCase(pageNumber = page).collect { state ->
                    when (state) {
                        NowPlayingResult.NetworkError -> {
                            _screenState.value = NowPlayingListState.Error(
                                errorMessage = R.string.network_error,
                                onRetry = { loadMovies(page) }
                            )
                        }

                        NowPlayingResult.UnexpectedError -> {
                            _screenState.value = NowPlayingListState.Error(
                                errorMessage = R.string.unexpected_error,
                                onRetry = { loadMovies(page) }
                            )
                        }

                        is NowPlayingResult.Success -> {
                            _screenState.value = NowPlayingListState.Idle(
                                playingInfo = state.result
                            )
                        }
                    }
                }
            }
        }
    }
}