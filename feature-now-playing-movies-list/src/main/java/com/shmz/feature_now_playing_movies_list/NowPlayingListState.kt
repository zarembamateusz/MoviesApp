package com.shmz.feature_now_playing_movies_list

import androidx.annotation.StringRes
import com.shmz.core_data.model.PlayingInfo

sealed interface NowPlayingListState {
    data object Loading : NowPlayingListState

    data class Idle(
        val playingInfo: PlayingInfo
    ) : NowPlayingListState

    data class Error(
        @StringRes val errorMessage: Int,
        val onRetry: () -> Unit
    ) : NowPlayingListState
}