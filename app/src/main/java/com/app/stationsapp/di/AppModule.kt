package com.app.stationsapp.di

import android.content.Context
import com.app.stationsapp.respository.StationsRepository
import com.app.stationsapp.respository.StationsRepositoryImpl
import com.app.stationsapp.util.SortStations
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideHelpRepository(@ApplicationContext context: Context, sortStations: SortStations) = StationsRepositoryImpl(context, sortStations) as StationsRepository

    @Singleton
    @Provides
    fun provideSortStations() = SortStations(LatLng(52.087966, 5.113372))
}