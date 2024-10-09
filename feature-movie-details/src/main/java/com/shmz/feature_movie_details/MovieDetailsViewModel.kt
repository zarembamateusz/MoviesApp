package com.shmz.feature_movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shmz.core_data.repositories.MovieResult
import com.shmz.core_domain.ChangeFavoriteStateUseCase
import com.shmz.core_domain.FetchMoveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val fetchMoveUseCase: FetchMoveUseCase
) : ViewModel() {

    private val _screenState: MutableStateFlow<MovieDetailsScreenState> =
        MutableStateFlow(MovieDetailsScreenState.Loading)
    val screenState: StateFlow<MovieDetailsScreenState> = _screenState

    fun onStart(movieId: Int) {
        observeMovie(movieId)
    }

    fun onFavoriteToggle(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            changeFavoriteStateUseCase(movieId, isFavorite)
        }
    }

    private fun observeMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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