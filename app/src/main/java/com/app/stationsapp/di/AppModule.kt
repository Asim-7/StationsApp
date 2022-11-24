package com.app.stationsapp.di

import com.app.stationsapp.respository.StationsRepository
import com.app.stationsapp.respository.StationsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * In the module, dependencies as added as singleton, which can be reached where needed
 * */
@Module
@InstallIn(SingletonComponent::class)     // helps to keep the dependencies alive as long as app is alive
object AppModule {

    @Singleton
    @Provides
    fun provideHelpRepository() = StationsRepositoryImpl() as StationsRepository
}