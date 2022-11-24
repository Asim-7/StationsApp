package com.app.stationsapp.respository

import com.app.stationsapp.model.StationData

interface StationsRepository {
    suspend fun getStationsData(): List<StationData>
}