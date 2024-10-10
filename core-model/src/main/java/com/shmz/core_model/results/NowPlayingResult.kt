package com.shmz.core_model.results

sealed interface NowPlayingResult {
    data class Success(
        val result: com.shmz.core_model.model.PlayingInfo
    ) : NowPlayingResult

    data object NetworkError : NowPlayingResult

    data object UnexpectedError : NowPlayingResult
}