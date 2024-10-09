package com.shmz.core_data.di

import com.shmz.core_data.NowPlayingRepository
import com.shmz.core_data.NowPlayingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DomainAndroidModule {
    @Binds
    fun nowPlayingViewModel(nowPlayingRepository: NowPlayingRepositoryImpl): NowPlayingRepository
}