package com.shmz.core_api.di

import com.shmz.core_api.BuildConfig
import com.shmz.core_api.Constant
import com.shmz.core_api.NowPlayingApi
import com.shmz.core_api.NowPlayingApiImpl
import com.shmz.core_api.interceptor.AuthInterceptor
import com.shmz.core_api.services.NowPlayingApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideBaseUrl(): String {
        return Constant.BASE_URL
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                provideAuthInterceptor()
            )
            .build()
    }

    @Provides
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(BuildConfig.MOVIES_API_KEY)
    }

    @Provides
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NowPlayingApiService =
        retrofit.create(NowPlayingApiService::class.java)

    @Provides
    @Singleton
    fun provideApi(apiService: NowPlayingApiService): NowPlayingApi =
        NowPlayingApiImpl(apiService)
}