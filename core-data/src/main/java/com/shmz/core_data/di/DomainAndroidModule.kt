package com.shmz.core_data.di

import com.shmz.core_data.repositories.MovieRepository
import com.shmz.core_data.repositories.MovieRepositoryImpl
import com.shmz.core_data.repositories.NowPlayingRepository
import com.shmz.core_data.repositories.NowPlayingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DomainAndroidModule {
    @Binds
    fun nowPlayingRepository(nowPlayingRepository: NowPlayingRepositoryImpl): NowPlayingRepository

    @Binds
    fun movieRepository(movieRepository: MovieRepositoryImpl): MovieRepository
}