package com.shmz.feature_movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shmz.core_domain.ChangeFavoriteStateUseCase
import com.shmz.core_domain.FetchMoveUseCase
import com.shmz.core_model.results.MovieResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val fetchMoveUseCase: FetchMoveUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _screenState.value = MovieDetailsScreenState.Error
    }
    private val _screenState: MutableStateFlow<MovieDetailsScreenState> =
        MutableStateFlow(MovieDetailsScreenState.Loading)
    val screenState: StateFlow<MovieDetailsScreenState> = _screenState

    fun onStart(movieId: Int) {
        observeMovie(movieId)
    }

    fun onFavoriteToggle(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            changeFavoriteStateUseCase(movieId, isFavorite)
        }
    }

    private fun observeMovie(movieId: Int) {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            fetchMoveUseCase(movieId).collect { movieResult ->
                _screenState.value = when (movieResult) {
                    MovieResult.NetworkError -> MovieDetailsScreenState.Error
                    is MovieResult.Success -> MovieDetailsScreenState.Idle(movie = movieResult.movie)
                    MovieResult.UnexpectedError -> MovieDetailsScreenState.Error
                }
            }
        }
    }
}