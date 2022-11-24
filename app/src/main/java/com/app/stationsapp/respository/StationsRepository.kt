package com.app.stationsapp.respository

interface StationsRepository {
    suspend fun getStationsData(): List<String>
}