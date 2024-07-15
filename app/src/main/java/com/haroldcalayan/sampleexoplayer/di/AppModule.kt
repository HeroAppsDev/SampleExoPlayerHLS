package com.haroldcalayan.sampleexoplayer.di

import com.haroldcalayan.sampleexoplayer.data.MediaRepository
import com.haroldcalayan.sampleexoplayer.data.MediaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    fun provideMediaRepository(): MediaRepository {
        return MediaRepositoryImpl()
    }
}