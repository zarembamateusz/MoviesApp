package com.shmz.core_database.di

import android.content.Context
import androidx.room.Room
import com.shmz.core_database.AppDatabase
import com.shmz.core_database.dao.MovieInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "movies_database"
        ).build()
    }

    @Provides
    fun provideMoviesInfoDao(db: AppDatabase): MovieInfoDao = db.movieInfoDao()
}