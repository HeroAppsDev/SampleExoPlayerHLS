package com.haroldcalayan.sampleexoplayer.di

import android.app.Application
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExoPlayerModule {

    @Provides
    @Singleton
    fun provideExoPlayer(application: Application): ExoPlayer {
        return ExoPlayer.Builder(application).build()
    }
}