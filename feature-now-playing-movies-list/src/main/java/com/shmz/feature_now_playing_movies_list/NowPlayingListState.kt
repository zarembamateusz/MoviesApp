package com.shmz.feature_now_playing_movies_list

import androidx.annotation.StringRes

sealed interface NowPlayingListState {
    data object Loading : NowPlayingListState

    data class Idle(
        val playingInfo: com.shmz.core_model.model.PlayingInfo
    ) : NowPlayingListState

    data class Error(
        @StringRes val errorMessage: Int,
        val onRetry: () -> Unit
    ) : NowPlayingListState
}