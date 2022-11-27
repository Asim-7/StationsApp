package com.app.stationsapp.di

import com.app.stationsapp.respository.StationsRepository
import com.app.stationsapp.respository.StationsRepositoryImpl
import com.app.stationsapp.util.SortStations
import com.app.stationsapp.viewmodel.StationsViewModel
import com.google.android.gms.maps.model.LatLng
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * In the module, dependencies as added as singleton, which can be reached where needed
 * */
val appModule = module {

    single<StationsRepository> {
        StationsRepositoryImpl(androidContext(), get())
    }

    single {
        SortStations(LatLng(52.087966, 5.113372))
    }

    viewModel {
        StationsViewModel(get())
    }
}