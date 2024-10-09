package com.shmz.feature_now_playing_movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shmz.core_data.NowPlayingRepository
import com.shmz.core_data.NowPlayingResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val nowPlayingRepository: NowPlayingRepository
) : ViewModel() {

    private val _screenState: MutableStateFlow<NowPlayingListState> =
        MutableStateFlow(NowPlayingListState.Loading)
    val screenState: StateFlow<NowPlayingListState> = _screenState

    fun onStart() {
        loadMovies(
            page = 1
        )
    }

    fun onFavoriteClick(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                nowPlayingRepository.changeFavoriteState(movieId, isFavorite)
            }.onSuccess {
                val currentState = screenState.value
                check(currentState is NowPlayingListState.Idle)
                val playingInfo = currentState.playingInfo
                val movies = playingInfo.movies.map { movie ->
                    if (movie.id == movieId) {
                        movie.copy(isFavorite = !movie.isFavorite)
                    } else {
                        movie
                    }
                }
                _screenState.value =
                    currentState.copy(playingInfo = playingInfo.copy(movies = movies))
            }
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
        viewModelScope.launch(Dispatchers.IO) {
            when (val state = nowPlayingRepository.fetchMovies(pageNumber = page)) {
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